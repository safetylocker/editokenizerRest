package editokenizerRest;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EdiContoller {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public EdiDocument greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new EdiDocument(counter.incrementAndGet(),
                String.format(template, name));
    }
}