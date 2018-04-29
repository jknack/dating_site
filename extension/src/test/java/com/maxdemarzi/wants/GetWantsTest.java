package com.maxdemarzi.wants;

import com.maxdemarzi.schema.Schema;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.harness.junit.Neo4jRule;
import org.neo4j.test.server.HTTP;

import java.util.ArrayList;
import java.util.HashMap;

public class GetWantsTest {
    @Rule
    public Neo4jRule neo4j = new Neo4jRule()
            .withFixture(FIXTURE)
            .withExtension("/v1", Wants.class)
            .withExtension("/v1", Schema.class);

    @Test
    public void shouldGetWants() {
        HTTP.POST(neo4j.httpURI().resolve("/v1/schema/create").toString());

        HTTP.Response response = HTTP.GET(neo4j.httpURI().resolve("/v1/users/maxdemarzi/wants").toString());
        ArrayList<HashMap> actual  = response.content();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldGetWantsWithUser() {
        HTTP.POST(neo4j.httpURI().resolve("/v1/schema/create").toString());

        HTTP.Response response = HTTP.GET(neo4j.httpURI().resolve("/v1/users/jexp/wants?username2=maxdemarzi").toString());
        ArrayList<HashMap> actual  = response.content();
        Assert.assertEquals(expected2, actual);
    }

    @Test
    public void shouldGetWantsLimited() {
        HTTP.POST(neo4j.httpURI().resolve("/v1/schema/create").toString());

        HTTP.Response response = HTTP.GET(neo4j.httpURI().resolve("/v1/users/maxdemarzi/wants?limit=1").toString());
        ArrayList<HashMap> actual  = response.content();
        Assert.assertTrue(actual.size() == 1);
        Assert.assertEquals(expected.get(0), actual.get(0));
    }

    @Test
    public void shouldGetWantsSince() {
        HTTP.POST(neo4j.httpURI().resolve("/v1/schema/create").toString());

        HTTP.Response response = HTTP.GET(neo4j.httpURI().resolve("/v1/users/maxdemarzi/wants?since=1490209400").toString());
        ArrayList<HashMap> actual  = response.content();
        Assert.assertTrue(actual.size() == 1);
        Assert.assertEquals(expected.get(0), actual.get(0));
    }

    private static final String FIXTURE =
            "CREATE (max:User {username:'maxdemarzi', " +
                    "email: 'max@neo4j.com', " +
                    "name: 'Max De Marzi'," +
                    "hash: '0bd90aeb51d5982062f4f303a62df935'," +
                    "password: 'swordfish'})" +
            "CREATE (jexp:User {username:'jexp', " +
                    "email: 'michael@neo4j.com', " +
                    "hash: '0bd90aeb51d5982062f4f303a62df935'," +
                    "name: 'Michael Hunger'," +
                    "password: 'tunafish'})" +
            "CREATE (laeg:User {username:'laexample', " +
                    "email: 'luke@neo4j.com', " +
                    "name: 'Luke Gannon'," +
                    "hash: '0bd90aeb51d5982062f4f303a62df935'," +
                    "password: 'cuddlefish'})" +
            "CREATE (fat:Attribute {name:'Fat'})" +
            "CREATE (bald:Attribute {name:'Bald'})" +
            "CREATE (jexp)-[:WANTS {time: 1490140299}]->(fat)" +
            "CREATE (laeg)-[:HAS {time: 1490208700}]->(bald)" +
            "CREATE (max)-[:WANTS {time: 1490209300 }]->(fat)" +
            "CREATE (max)-[:WANTS {time: 1490209400 }]->(bald)";

    private static final ArrayList<HashMap<String, Object>> expected = new ArrayList<HashMap<String, Object>>() {{
        add(new HashMap<String, Object>() {{
            put("name", "Bald");
            put("time", 1490209400);
            put("has", 1);
            put("wants", 1);
            put("have", false);
            put("want", false);
        }});
        add(new HashMap<String, Object>() {{
            put("name", "Fat");
            put("time", 1490209300);
            put("has", 0);
            put("wants", 2);
            put("want", false);
            put("have", false);
        }});
    }};

    private static final ArrayList<HashMap<String, Object>> expected2 = new ArrayList<HashMap<String, Object>>() {{
        add(new HashMap<String, Object>() {{
            put("name", "Fat");
            put("time", 1490140299);
            put("has", 0);
            put("wants", 2);
            put("want", true);
            put("have", true);
        }});
    }};
}
