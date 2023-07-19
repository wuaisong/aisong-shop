package wu.ai.song.api.test;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import wu.ai.song.api.controller.MockController;
import wu.ai.song.api.service.Demo;
import wu.ai.song.api.utils.Result;

@WebMvcTest(MockController.class)
class TestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Demo demo;

    @Test
    public void query() throws Exception {
        Result defaultValue = Result.success("default ---> test");
        Mockito.when(demo.startTwoSeckil(1, 1)).thenReturn(defaultValue);
        mockMvc.perform(MockMvcRequestBuilders.get("/startTwoMock").param("redPacketId", "1")).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
