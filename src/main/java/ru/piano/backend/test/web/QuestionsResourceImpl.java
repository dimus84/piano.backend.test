
package ru.piano.backend.test.web;

import javax.annotation.Resource;
import javax.ws.rs.Path;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
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
        String questionsResponse = questionsTarget.request(MediaType.APPLICATION_JSON_TYPE).get(String.class);

        Questions list = new Questions();
        list.withQuestions(new Question().withTitle(questionsResponse));

        return list;
    }

}
