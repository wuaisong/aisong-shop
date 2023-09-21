package wu.ai.song.api.view;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author wuyaming
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class ViewController {
    private final ThymeleafViewResolver thymeleafViewResolver;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    @GetMapping(value = "/test", produces = MediaType.TEXT_HTML_VALUE)
    public String startTwo(Model model) {
        model.addAttribute("test", ThreadLocalRandom.current().nextDouble());
        WebContext ctx =
            new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        String process = thymeleafViewResolver.getTemplateEngine().process("test", ctx);
        log.info(process);
        return process;
    }

}
