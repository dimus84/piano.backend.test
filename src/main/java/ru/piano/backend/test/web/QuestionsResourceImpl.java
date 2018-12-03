
package ru.piano.backend.test.web;

import static com.google.common.net.MediaType.JSON_UTF_8;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ws.rs.Path;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.piano.backend.test.api.QuestionsResource;
import ru.piano.backend.test.view.Question;
import ru.piano.backend.test.view.Questions;

@Path("questions")
public class QuestionsResourceImpl implements QuestionsResource {

    @Resource(mappedName = "ru.piano.backend.test.stackexchange.api.url")
    private String stackexchangeApiUrl;

    public Questions list(Integer page, Integer pagesize, String order, String sort, String intitle) {

        Client client = ClientBuilder.newClient();
        // page=1&pagesize=5&order=desc&sort=activity&intitle=java&site=stackoverflow
        WebTarget questionsTarget = client.target(stackexchangeApiUrl + "/search")
                .queryParam("page", 1)
                .queryParam("pagesize", 5)
                .queryParam("order", "desc")
                .queryParam("sort", "activity")
                .queryParam("intitle", "java")
                .queryParam("site", "stackoverflow");
        String jsonResponse = questionsTarget.request(JSON_UTF_8.toString()).acceptEncoding("UTF-8").get(String.class);

        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(jsonResponse);
        } catch (ParseException ex) {
            Logger.getLogger(QuestionsResourceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        Questions list = new Questions();
        list.withQuestions(new Question(), new Question(), new Question());

        return list;
    }

}
