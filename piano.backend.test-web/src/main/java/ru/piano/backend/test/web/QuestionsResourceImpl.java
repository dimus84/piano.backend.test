
package ru.piano.backend.test.web;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import javax.annotation.Resource;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.ext.xml.XSLTTransform;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import ru.piano.backend.test.api.QuestionsResource;
import ru.piano.backend.test.utils.JaxbHelper;
import ru.piano.backend.test.view.Question;
import ru.piano.backend.test.view.QuestionsList;

@Path("questions")
public class QuestionsResourceImpl implements QuestionsResource {

    @Resource(mappedName = "ru.piano.backend.test.stackexchange.api.url")
    private String stackexchangeApiUrl;

    @Autowired
    private JaxbHelper jaxbHelper;

    @Override
    @XSLTTransform(value = "stylesheets/piano.backend.test/questionsList.xsl", mediaTypes = "application/xhtml+xml", type = XSLTTransform.TransformType.SERVER)
    public QuestionsList list(Integer page, Integer pagesize, String order, String sort, String intitle) {

        if (page != null && page < 0) page = 1;
        if (pagesize != null && pagesize < 1) pagesize = 5;
        if (order == null || order.isEmpty()) order = "desc";
        if (sort == null || sort.isEmpty()) sort = "activity";

        QuestionsList list = new QuestionsList()
                .withPage(page)
                .withPagesize(pagesize)
                .withOrder(order)
                .withSort(sort)
                .withIntitle(intitle);

        if (list.getIntitle() != null && !list.getIntitle().isEmpty()) {
            Client client = ClientBuilder.newClient();
            // page=1&pagesize=5&order=desc&sort=activity&intitle=java&site=stackoverflow
            WebTarget questionsTarget = client.target(stackexchangeApiUrl + "/search")
                    .queryParam("page", list.getPage())
                    .queryParam("pagesize", list.getPagesize())
                    .queryParam("order", list.getOrder())
                    .queryParam("sort", list.getSort())
                    .queryParam("intitle", list.getIntitle())
                    .queryParam("site", "stackoverflow");
            Response questionsResponse = questionsTarget.request().acceptEncoding("*;q=0").get();
            String jsonResponseString;
            try {
                jsonResponseString = IOUtils.toString(new GZIPInputStream(questionsResponse.readEntity(InputStream.class)), "UTF-8");
            } catch (IOException ex) {
                Logger.getLogger(QuestionsResourceImpl.class.getName()).log(Level.SEVERE, null, ex);
                throw new WebApplicationException(ex);
            }

//            InputStream jsonResponseStream = QuestionsResourceImpl.class.getResourceAsStream("./QuestionsResource/stackexchangeResponse.json");
//            try {
//                jsonResponseString = IOUtils.toString(jsonResponseStream, "UTF-8");
//            } catch (IOException ex) {
//                Logger.getLogger(QuestionsResourceImpl.class.getName()).log(Level.SEVERE, null, ex);
//                throw new WebApplicationException("Ошибка чтения файла ./QuestionsResource/stackexchangeResponse.json", 450);
//            }

            JSONParser parser = new JSONParser();
            JSONObject jsonResponse;
            try {
                jsonResponse = (JSONObject) parser.parse(jsonResponseString);
            } catch (ParseException ex) {
                Logger.getLogger(QuestionsResourceImpl.class.getName()).log(Level.SEVERE, null, ex);
                throw new WebApplicationException(ex);
            }
            JSONArray jsonItems = (JSONArray) jsonResponse.get("items");
            jsonItems.forEach(item -> {
                Question q = jaxbHelper.unmarshal(item.toString(), Question.class, "application/json");

                LocalDateTime crd = LocalDateTime.ofEpochSecond((Long) ((JSONObject) item).get("creation_date"), 0, ZoneOffset.UTC);
                q.setCreationDate(crd);

                if (((JSONObject) item).get("last_edit_date") != null) {
                    LocalDateTime led = LocalDateTime.ofEpochSecond((Long) ((JSONObject) item).get("last_edit_date"), 0, ZoneOffset.UTC);
                    q.setLastEditDate(led);
                }

                if (((JSONObject) item).get("last_activity_date") != null) {
                    LocalDateTime lad = LocalDateTime.ofEpochSecond((Long) ((JSONObject) item).get("last_activity_date"), 0, ZoneOffset.UTC);
                    q.setLastActivityDate(lad);
                }

                list.withQuestions(q);

            });
        }

        return list;
    }

}
