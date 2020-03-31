package Neo4j;

import Entity.*;
import org.neo4j.driver.*;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Relationship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Neo4jAPI {
    public Neo4jAPI() {
    }

    public Relation[] relationsByType(String type, Session session) {
        List<Relation> result = new ArrayList();
        Result queryResult = session.run("MATCH (n:Ontology)-[r: `" + type + "`]->(m:Ontology) RETURN n,m");

        while(queryResult.hasNext()) {
            Record record = queryResult.next();
            String subjectName = record.get("n").get("name").asString();
            String objectName = record.get("m").get("name").asString();
            Relation relation = new Relation(subjectName, objectName);
            result.add(relation);
        }

        return (Relation[])result.toArray(new Relation[result.size()]);
    }

    public String[] entitiesByType(String type, Session session) {
        List<String> result = new ArrayList();
        Result queryResult = session.run("MATCH (n:Ontology { type: $type}) RETURN n", Values.parameters(new Object[]{"type", type}));

        while(queryResult.hasNext()) {
            Record record = queryResult.next();
            String name = record.get("n").get("name").asString();
            result.add(name);
        }

        return (String[])result.toArray(new String[result.size()]);
    }

    public Recursion downwardRecursion(String entity, Session session) {
        Recursion result = new Recursion();
        result.name = entity;
        queryEntityIsNest(result, session);
        if (null == result.type) {
            result = null;
        }

        return result;
    }

    public String upwardRecursion(String predicateID, Session session) {
        String result = "";
        String cql = "MATCH (n:Ontology)-[r{id:'" + predicateID + "'}]->(m:Ontology) ";
        cql = cql + "WITH n,m ";
        cql = cql + "MATCH (o:Ontology)-[:subject]->(n) ";
        cql = cql + "WHERE exists((o:Ontology)-[:object]->(m)) ";
        cql = cql + "RETURN o";

        String name;
        for(Result queryResult = session.run(cql); queryResult.hasNext(); result = name) {
            Record record = queryResult.next();
            name = record.get("o").get("name").asString();
        }

        return result;
    }



    private static void queryEntityIsNest(Recursion result, Session session) {
        String cql = "MATCH (n:Ontology { name: $entity}) RETURN n,exists((n)-[:predicate]->(:Ontology)) as nested";
        Result queryResult = session.run(cql, Values.parameters(new Object[]{"entity", result.name}));

        while(queryResult.hasNext()) {
            Record record = queryResult.next();
            Node node = record.get("n").asNode();
            recursionFillByNode(result, node);
            boolean nested = record.get("nested").asBoolean();
            if (nested) {
                querySubEntities(result, session);
            }
        }

    }

    private static void querySubEntities(Recursion result, Session session) {
        String name = result.name;
        String cql = "Match (n:Ontology{name:$name})-[r:predicate|:subject|:object]->(m:Ontology) return n,r,m";
        Result queryResult = session.run(cql, Values.parameters(new Object[]{"name", name}));
        if (result.isDefinition) {
            List<Record> records = resultToList(queryResult);
            List<String> predicts = new ArrayList();
            List<String> subjects = new ArrayList();
            List<String> objects = new ArrayList();
            Iterator var8 = records.iterator();

            while(var8.hasNext()) {
                Record record = (Record)var8.next();
                String type = record.get("r").asRelationship().type();
                Node mNode = record.get("m").asNode();
                String mName = mNode.get("name").asString();
                if ("predicate".equals(type)) {
                    predicts.add(mName);
                } else if ("subject".equals(type)) {
                    subjects.add(mName);
                } else if ("object".equals(type)) {
                    objects.add(mName);
                }
            }

            String sroCql = "MATCH (n:Ontology)-[r]->(m:Ontology) WHERE n.name in ";
            sroCql = sroCql + arrayToString(subjects);
            sroCql = sroCql + " and type(r) in ";
            sroCql = sroCql + arrayToString(predicts);
            sroCql = sroCql + " and m.name in ";
            sroCql = sroCql + arrayToString(objects);
            sroCql = sroCql + " return n,r,m";
            Result querySroResult = session.run(sroCql);
            List<Record> sroRecords = resultToList(querySroResult);
            if (null != sroRecords && !sroRecords.isEmpty()) {
                String predict = null;
                Node subjectNode = null;
                Node objectNode = null;
                Iterator var14 = sroRecords.iterator();

                while(var14.hasNext()) {
                    Record record = (Record)var14.next();
                    Node nNode = record.get("n").asNode();
                    String subjectName = nNode.get("name").asString();
                    String subjectType = nNode.get("type").asString();
                    String rTypt = record.get("r").asRelationship().type();
                    Node mNode = record.get("m").asNode();
                    String objName = mNode.get("name").asString();
                    if (name.equals(getNestName(subjectName, subjectType, rTypt, objName))) {
                        subjectNode = nNode;
                        predict = rTypt;
                        objectNode = mNode;
                        break;
                    }
                }

                if (null == predict || null == subjectNode || null == objectNode) {
                    Record record = (Record)sroRecords.get(0);
                    subjectNode = record.get("n").asNode();
                    predict = record.get("r").asRelationship().type();
                    objectNode = record.get("m").asNode();
                }

                result.predicate = predict;
                Recursion subjectRecursion = new Recursion();
                subjectRecursion.name = subjectNode.get("name").asString();
                queryEntityIsNest(subjectRecursion, session);
                result.subject = subjectRecursion;
                Recursion objRecursion = new Recursion();
                objRecursion.name = objectNode.get("name").asString();
                queryEntityIsNest(objRecursion, session);
                result.object = objRecursion;
            }
        } else {
            while(true) {
                while(queryResult.hasNext()) {
                    Record record = queryResult.next();
                    String type = record.get("r").asRelationship().type();
                    Node mNode = record.get("m").asNode();
                    if ("predicate".equals(type)) {
                        result.predicate = mNode.get("name").asString();
                    } else if ("subject".equals(type) || "object".equals(type)) {
                        Recursion subRecursion = new Recursion();
                        subRecursion.name = mNode.get("name").asString();
                        queryEntityIsNest(subRecursion, session);
                        if ("subject".equals(type)) {
                            result.subject = subRecursion;
                        } else if ("object".equals(type)) {
                            result.object = subRecursion;
                        }
                    }
                }

                return;
            }
        }

    }

    private static String getNestName(String subject, String subjectType, String rel, String obj) {
        String nestName = subject + rel + obj;
        if ("修饰限定".equals(rel)) {
            boolean subjectFirst = true;
            if ("程度修饰语".equals(subjectType)) {
                subjectFirst = false;
            }

            if (subjectFirst) {
                nestName = subject + obj;
            } else {
                nestName = obj + subject;
            }
        } else if ("否定修饰".equals(rel)) {
            nestName = subject + obj;
        }

        return nestName;
    }

    private static void recursionFillByNode(Recursion result, Node node) {
        String name = node.get("name").asString();
        String type = node.get("type").asString();
        boolean isDefinition = node.get("isDefinition").asBoolean();
        Value genderValue = node.get("gender");
        Integer gender = null;
        if (!genderValue.isNull()) {
            gender = Integer.valueOf(genderValue.asString());
        }

        List<String> position = new ArrayList();
        Value positionValue = node.get("position");
        if (!positionValue.isNull()) {
            List<Object> positions = positionValue.asList();
            Iterator var10 = positions.iterator();

            while(var10.hasNext()) {
                Object po = var10.next();
                position.add((String)po);
            }
        }

        String[] positionArray = (String[])position.toArray(new String[position.size()]);
        Boolean subjectivity = null;
        Value subjectivityValue = node.get("subjectivity");
        if (!subjectivityValue.isNull()) {
            String subjectivityString = subjectivityValue.asString();
            subjectivity = "是".equals(subjectivityString);
        }

        result.name = name;
        result.type = type;
        result.isDefinition = isDefinition;
        result.gender = gender;
        result.position = positionArray;
        result.subjectivity = subjectivity;
    }

    private static List<Record> resultToList(Result result) {
        ArrayList records = new ArrayList();

        while(result.hasNext()) {
            Record record = result.next();
            records.add(record);
        }

        return records;
    }

    private static String arrayToString(List<String> array) {
        String result = "[";
        if (null != array && !array.isEmpty()) {
            result = result + "'";
            int endIndex = array.size() - 1;

            for(int i = 0; i < array.size(); ++i) {
                String str = (String)array.get(i);
                if (i != endIndex) {
                    result = result + str + "','";
                } else {
                    result = result + str;
                }
            }

            result = result + "'";
        }

        return result + "]";
    }

    public static Neighbour[] neighbours(String entity, Session session) {
        List<Neighbour> result = new ArrayList();
        String cql = "MATCH (n:Ontology{ name: $name})-[r]-(m:Ontology)";
        cql = cql + "WHERE NOT type(r) IN['subject', 'predicate', 'object'] RETURN r, m";
        Result queryResult = session.run(cql, Values.parameters(new Object[]{"name", entity}));

        while(queryResult.hasNext()) {
            Record record = queryResult.next();
            Relationship relation = record.get("r").asRelationship();
            long endNodeId = relation.endNodeId();
            Node mNode = record.get("m").asNode();
            long mId = mNode.id();
            boolean neighbourIsObject = endNodeId == mId;
            String predicate = relation.type();
            String predicateID = relation.get("id").asString();
            List<Object> sourceList = relation.get("source").asList();
            List<String> sources = new ArrayList();
            Iterator var16 = sourceList.iterator();

            while(var16.hasNext()) {
                Object source = var16.next();
                sources.add((String)source);
            }

            String[] sourceArray = (String[])sources.toArray(new String[sources.size()]);
            String name = mNode.get("name").asString();
            String type = mNode.get("type").asString();
            Integer confidence = null;
            if ("导致".equals(predicate)) {
                confidence = relation.get("confidence").asInt();
            }

            Neighbour neighbour = new Neighbour(neighbourIsObject, predicate, predicateID, sourceArray, type, name, confidence);
            result.add(neighbour);
        }

        return (Neighbour[])result.toArray(new Neighbour[result.size()]);
    }
}
