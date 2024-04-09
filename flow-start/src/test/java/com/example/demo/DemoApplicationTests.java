package com.example.demo;

import com.example.demo.flow.BpmnUtil;
import com.example.demo.flow.ExclusiveBranch;
import com.example.demo.flow.Inout;
import com.example.demo.flow.ProcessNode;
import org.flowable.bpmn.BpmnAutoLayout;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.EndEvent;
import org.flowable.bpmn.model.ExclusiveGateway;
import org.flowable.bpmn.model.MultiInstanceLoopCharacteristics;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.bpmn.model.UserTask;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.flowable.bpmn.model.Process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
        BpmnModel model = new BpmnModel();
        Process process = new Process();
        model.addProcess(process);
        process.setId("Process_" + UUID.randomUUID());
        process.setExecutable(true);
    }

    public String toBpmn(ProcessNode node) {
        // 一.准备工作
        BpmnModel bpmnModel = new BpmnModel();
        Process process = new Process(); // 相当于图纸
        bpmnModel.addProcess(process);
        process.setId("Process_" + UUID.randomUUID());
        process.setExecutable(true);
        // 二.开始结束节点
        StartEvent startEvent = new StartEvent();// 新建开始节点
        startEvent.setId("_start");
        process.addFlowElement(startEvent);// 绘制到图纸
        EndEvent endEvent = new EndEvent(); // 新建结束节点
        endEvent.setId("_end");// 绘制到图纸
        process.addFlowElement(endEvent);
        // 三.递归绘制节点
        drawNode(process, node, "_start", "_end", null);
        // 四.自动布局
        new BpmnAutoLayout(bpmnModel).execute();
        // 五.转xml
        BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
        byte[] convertToXML = bpmnXMLConverter.convertToXML(bpmnModel);
        String xml = new String(convertToXML);
        xml = xml.replaceAll("<", "<").replaceAll(">", ">");
        return xml;
    }

    /**
     * 绘制节点
     *
     * @param process       bpmn process 图纸
     * @param node          json的节点
     * @param preId         上一节点id
     * @param endId         结束节点
     * @param preExpression 上一节点表达式
     */
    public void drawNode(Process process, ProcessNode node, String preId, String endId, String preExpression) {
        // 根据type绘制不同种类的节点
        Inout inout = drawNodeByType(process, node);
        // 绘制前一根线
        process.addFlowElement(createSequenceFlow(preId, inout.getIn(), preExpression));
        if (node.getNext() == null) {
            // 没有下一步, 绘制指向结束的线
            process.addFlowElement(createSequenceFlow(inout.getOut(), endId, null));
        } else {
            // 有下一步, 递归绘制下一个节点
            drawNode(process, node.getNext(), inout.getOut(), endId, null);
        }
    }

    /**
     * 绘制不同种类节点
     *
     * @param process
     * @param node
     * @return
     */
    private Inout drawNodeByType(Process process, ProcessNode node) {
        if (node.getType().equals("审核节点")) {
            return drawAuditNode(process, node);
        } else if (node.getType().equals("分支节点")) {
            return drawExclusiveNode(process, node);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 绘制审核节点
     *
     * @param process
     * @param node
     * @return
     */
    private Inout drawAuditNode(Process process, ProcessNode node) {
        // 绘制节点
        String id = "Node_" + UUID.randomUUID();
        UserTask userTask = new UserTask();
        userTask.setId(id);
        userTask.setName(node.getName());
        // 设置多实例
        userTask.setAssignee("${user}");
        MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = new MultiInstanceLoopCharacteristics();
        if (node.getAssignee().getMultiMode().equals("顺序审批")) {
            multiInstanceLoopCharacteristics.setSequential(true);
        }
        multiInstanceLoopCharacteristics.setElementVariable("user");
        // 完成条件
        multiInstanceLoopCharacteristics.setCompletionCondition("${nrOfInstances == nrOfCompletedInstances}");
        multiInstanceLoopCharacteristics.setInputDataItem("${users}");
        userTask.setLoopCharacteristics(multiInstanceLoopCharacteristics);
        // 保存json节点配置到扩展属性
        Map<String, Object> extensions = new HashMap<>();
        extensions.put("node", node);
        BpmnUtil.addExtensionProperty(userTask, extensions);
        return new Inout(id, id);
    }

    /**
     * 绘制分支节点
     *
     * @param process
     * @param node
     * @return
     */
    private Inout drawExclusiveNode(Process process, ProcessNode node) {
        // 开始网关
        String startId = "Exclusive_" + UUID.randomUUID();
        ExclusiveGateway startGateway = new ExclusiveGateway();
        startGateway.setId(startId);
        process.addFlowElement(startGateway);
        // 结束网关
        String endId = "Exclusive_" + UUID.randomUUID();
        ExclusiveGateway endGateway = new ExclusiveGateway();
        endGateway.setId(endId);
        process.addFlowElement(endGateway);
        // 绘制分支
        List<ExclusiveBranch> branches = node.getExclusive();
        for (ExclusiveBranch branch : branches) {
            String expression = branch.getCondition();
            if (branch.getProcess() == null) {
                // 没有子流程，直接绘制结束线
                process.addFlowElement(createSequenceFlow(startId, endId, expression));
            } else {
                // 有子流程，递归绘制子流程
                drawNode(process, branch.getProcess(), startId, endId, expression);
            }
        }
        // int和out不一样
        return new Inout(startId, endId);
    }

    /**
     * 创建连线
     *
     * @param from
     * @param to
     * @return
     */
    public SequenceFlow createSequenceFlow(String from, String to, String conditionExpression) {
        SequenceFlow flow = new SequenceFlow();
        flow.setId(from + "-" + to);
        flow.setSourceRef(from);
        flow.setTargetRef(to);
        if (conditionExpression != null) {
            flow.setConditionExpression(conditionExpression);
        }
        return flow;
    }
}
