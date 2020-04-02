import Entity.Neighbour;
import Entity.Recursion;
import Entity.Relation;
import Neo4j.Neo4jAPI;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class App {
    public static void main( String[] args ) throws IOException {
        Neo4jAPI neo4j = new Neo4jAPI();

        String neoUser = "neo4j";
        String neoPassword = "3203";
        String neoHost = "localhost";
        String boltPort = "7687";
        String uri = "bolt://" + neoHost + ":" + boltPort;
        Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(neoUser, neoPassword));
        Session session = driver.session();

        LocalDateTime start = LocalDateTime.now();

        //Relation[] test = neo4j.relationsByType("导致", session);
        /*for (int i=0; i < test.length; i++) {
            System.out.print("subjects:" + test[i].subject +"\nobjects:" + test[i].object + "\n");
        }*/


        //String[] test = neo4j.entitiesByType("症状", session);
        /*for (int i=0; i<test.length; i++) {
            System.out.print(test[i] + "\n");
        }*/


        /*Recursion test = neo4j.downwardRecursion("月经前妊娠期体温高于正常", session);*/


        //System.out.print(neo4j.upwardRecursion("105fe97072f211ea8c3d9eb6d0c289e1", session));


        Neighbour[] test = neo4j.neighbours("体温", session);
        for (int i = 0; i < test.length; i++) {
            System.out.print(test[i].name + "\n");
        }

        LocalDateTime end = LocalDateTime.now();

        System.out.print("\nduration:" + Duration.between(start,end));

        driver.close();
    }
}
