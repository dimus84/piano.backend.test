
package ru.piano.backend.test.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.apache.cxf.helpers.IOUtils;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.piano.backend.test.api.QuestionsResource;
import ru.piano.backend.test.utils.JaxbHelper;
import ru.piano.backend.test.view.Question;
import ru.piano.backend.test.view.Questions;

@Path("questions")
public class QuestionsResourceImpl implements QuestionsResource {

    @Resource(mappedName = "ru.piano.backend.test.stackexchange.api.url")
    private String stackexchangeApiUrl;

    private final JAXBContext jaxbContext;

    public QuestionsResourceImpl() throws JAXBException {
        jaxbContext = JAXBContextFactory.createContext(new Class[]{Question.class}, null);
    }

    public Questions list(Integer page, Integer pagesize, String order, String sort, String intitle) {

//        Client client = ClientBuilder.newClient();
//        // page=1&pagesize=5&order=desc&sort=activity&intitle=java&site=stackoverflow
//        WebTarget questionsTarget = client.target(stackexchangeApiUrl + "/search")
//                .queryParam("page", 1)
//                .queryParam("pagesize", 5)
//                .queryParam("order", "desc")
//                .queryParam("sort", "activity")
//                .queryParam("intitle", "java")
//                .queryParam("site", "stackoverflow");
//        String jsonResponse = questionsTarget.request(JSON_UTF_8.toString()).acceptEncoding("UTF-8").get(String.class);

        InputStream jsonResponseStream = QuestionsResourceImpl.class.getResourceAsStream("./QuestionsResource/stackexchangeResponse.json");
        String jsonResponseString;
        try {
            jsonResponseString = IOUtils.toString(jsonResponseStream, "UTF-8");
        } catch (IOException ex) {
            Logger.getLogger(QuestionsResourceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new WebApplicationException("Ошибка чтения файла ./QuestionsResource/stackexchangeResponse.json", 450);
        }

        JSONParser parser = new JSONParser();
        JSONObject jsonResponse;
        try {
            jsonResponse = (JSONObject) parser.parse(jsonResponseString);
        } catch (ParseException ex) {
            Logger.getLogger(QuestionsResourceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new WebApplicationException(ex);
        }
        JSONArray jsonItems = (JSONArray) jsonResponse.get("items");
        Questions list = new Questions();
        jsonItems.forEach(item -> {
            Question q = JaxbHelper.unmarshal(jaxbContext, item.toString(), Question.class, "application/json");
            list.withQuestions(q);

        });

        return list;
    }

}
