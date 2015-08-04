package com.hxr.javatone.neo4j;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class EduDemo {

    static GraphDatabaseService graphDb = null;
    static Node firstNode;
    static Node secondNode;
    static Relationship relationship;
    static List<Link> links = new ArrayList<Link>();

    private static enum RelTypes implements RelationshipType {
        LINK
    }

    private static enum MyLabels implements Label {
        DEFAULT
    }

    public static void main(final String[] args) {
        String DB_PATH = "/Users/hanxirui/Documents/workspace/library/neo4j-community-2.2.2/data/graph.db";
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
        Transaction tx = graphDb.beginTx();

        try {
            for (Link t_link : links) {
                firstNode = graphDb.findNode(MyLabels.DEFAULT, "name", t_link.getStart());
                if (firstNode == null) {
                    firstNode = graphDb.createNode();
                    firstNode.addLabel(MyLabels.DEFAULT);
                    firstNode.setProperty("name", t_link.getStart());
                }

                secondNode = graphDb.findNode(MyLabels.DEFAULT, "name", t_link.getEnd());
                if (secondNode == null) {
                    secondNode = graphDb.createNode();
                    secondNode.addLabel(MyLabels.DEFAULT);
                    secondNode.setProperty("name", t_link.getEnd());
                }

                relationship = firstNode.createRelationshipTo(secondNode, RelTypes.LINK);

            }
//            最短路径查找
//            START d=node(92), e=node(1)
//                    MATCH p = shortestPath( d-[*..10]->e )
//                    RETURN d,e,p
            tx.success();
        } finally {
            registerShutdownHook(graphDb);
            tx.close();
        }

    }

    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }

    static {

        Link link1= new Link();link1.setStart("e7f644b4-8d42-399b-bb7b-2183edbaf6c2");link1.setEnd("81d016b7-364e-3da8-9b90-9950cde72318");links.add(link1);
        Link link2= new Link();link2.setStart("9c670502-b73c-389b-9247-70f2d4f3cd25");link2.setEnd("81d016b7-364e-3da8-9b90-9950cde72318");links.add(link2);
        Link link3= new Link();link3.setStart("8923ed7b-ba8b-3745-ae9b-c4c9921f3bd3");link3.setEnd("81d016b7-364e-3da8-9b90-9950cde72318");links.add(link3);
        Link link4= new Link();link4.setStart("5f9cd43c-1ea7-33fb-be55-ab923828ef0c");link4.setEnd("81d016b7-364e-3da8-9b90-9950cde72318");links.add(link4);
        Link link5= new Link();link5.setStart("73f657de-23ae-3242-ba01-176ecb77f888");link5.setEnd("81d016b7-364e-3da8-9b90-9950cde72318");links.add(link5);
        Link link6= new Link();link6.setStart("a1677c03-f73f-36a3-a6c4-2120459e1b7c");link6.setEnd("81d016b7-364e-3da8-9b90-9950cde72318");links.add(link6);
        Link link7= new Link();link7.setStart("4f353012-8989-3d2f-9ecf-2021811dadf8");link7.setEnd("81d016b7-364e-3da8-9b90-9950cde72318");links.add(link7);
        Link link8= new Link();link8.setStart("6a78964a-b883-32d7-92c6-99d4a5f2ac60");link8.setEnd("81d016b7-364e-3da8-9b90-9950cde72318");links.add(link8);
        Link link9= new Link();link9.setStart("6e13a6fe-4de5-345e-bef5-df504ebd711b");link9.setEnd("81d016b7-364e-3da8-9b90-9950cde72318");links.add(link9);
        Link link10= new Link();link10.setStart("e924d3bf-92fe-338e-946d-3ec814fd154b");link10.setEnd("81d016b7-364e-3da8-9b90-9950cde72318");links.add(link10);
        Link link11= new Link();link11.setStart("b61b7ea0-1ddf-3df7-a2c4-cca6ccfa2038");link11.setEnd("81d016b7-364e-3da8-9b90-9950cde72318");links.add(link11);
        Link link12= new Link();link12.setStart("f8556bf0-3a05-3f10-9d8b-c3782eb790b6");link12.setEnd("81d016b7-364e-3da8-9b90-9950cde72318");links.add(link12);
        Link link13= new Link();link13.setStart("cea8b3de-3b65-379c-80be-f1c7128a2440");link13.setEnd("81d016b7-364e-3da8-9b90-9950cde72318");links.add(link13);
        Link link14= new Link();link14.setStart("91135d40-acf2-36dd-aea7-25fa4dc2e64f");link14.setEnd("81d016b7-364e-3da8-9b90-9950cde72318");links.add(link14);
        Link link15= new Link();link15.setStart("67b82697-28b2-34f8-8034-e23b1083d9d9");link15.setEnd("81d016b7-364e-3da8-9b90-9950cde72318");links.add(link15);
        Link link16= new Link();link16.setStart("3ec39b5f-f635-39d4-9545-c7030963541a");link16.setEnd("81d016b7-364e-3da8-9b90-9950cde72318");links.add(link16);
        Link link17= new Link();link17.setStart("86f693d7-a556-3eda-a576-1109f888d172");link17.setEnd("81d016b7-364e-3da8-9b90-9950cde72318");links.add(link17);
        Link link18= new Link();link18.setStart("c1951191-6043-3371-a869-0f96c98f48a3");link18.setEnd("81d016b7-364e-3da8-9b90-9950cde72318");links.add(link18);
        Link link19= new Link();link19.setStart("c37ad897-5890-3941-aa27-0b9bd2145ddc");link19.setEnd("81d016b7-364e-3da8-9b90-9950cde72318");links.add(link19);
        Link link20= new Link();link20.setStart("662ab119-9e28-3010-a691-69bde357873e");link20.setEnd("81d016b7-364e-3da8-9b90-9950cde72318");links.add(link20);
        Link link21= new Link();link21.setStart("48885b7e-5682-3098-af54-1339abc059a6");link21.setEnd("81d016b7-364e-3da8-9b90-9950cde72318");links.add(link21);
        Link link22= new Link();link22.setStart("bd6f227b-e4f6-3056-ac62-fd1ce8075e26");link22.setEnd("81d016b7-364e-3da8-9b90-9950cde72318");links.add(link22);
        Link link23= new Link();link23.setStart("0b639481-b306-3f45-a6ca-b0fc6108e251");link23.setEnd("81d016b7-364e-3da8-9b90-9950cde72318");links.add(link23);
        Link link24= new Link();link24.setStart("400bd759-a1c8-3392-a434-d4239175c655");link24.setEnd("81d016b7-364e-3da8-9b90-9950cde72318");links.add(link24);
        Link link25= new Link();link25.setStart("f61ff0ab-54d1-3f63-9c2e-6e3f84d1549e");link25.setEnd("81d016b7-364e-3da8-9b90-9950cde72318");links.add(link25);
        Link link26= new Link();link26.setStart("91a28ced-fc28-30d3-9187-f27f5dc26bef");link26.setEnd("81d016b7-364e-3da8-9b90-9950cde72318");links.add(link26);
        Link link27= new Link();link27.setStart("faf993ea-1f65-334b-b215-3b4b2f5fad37");link27.setEnd("81d016b7-364e-3da8-9b90-9950cde72318");links.add(link27);
        Link link28= new Link();link28.setStart("5015324a-37af-36d7-bedf-19d66698b486");link28.setEnd("81d016b7-364e-3da8-9b90-9950cde72318");links.add(link28);
        Link link29= new Link();link29.setStart("33c74519-0bfb-3276-8677-c039a8cbc32e");link29.setEnd("81d016b7-364e-3da8-9b90-9950cde72318");links.add(link29);
        Link link30= new Link();link30.setStart("3b910f1c-21f6-3500-a568-77a600c90d6a");link30.setEnd("81d016b7-364e-3da8-9b90-9950cde72318");links.add(link30);
        Link link31= new Link();link31.setStart("5b924ab0-7172-3dd6-bb89-15f45db4d821");link31.setEnd("81d016b7-364e-3da8-9b90-9950cde72318");links.add(link31);
        Link link32= new Link();link32.setStart("24ffafc7-d10d-30b3-baa8-49bedde95655");link32.setEnd("81d016b7-364e-3da8-9b90-9950cde72318");links.add(link32);
        Link link33= new Link();link33.setStart("ed09a671-d73c-3c6f-acd9-531412075009");link33.setEnd("91a28ced-fc28-30d3-9187-f27f5dc26bef");links.add(link33);
        Link link34= new Link();link34.setStart("a40084d8-c0e2-3212-a169-53dfbbda969e");link34.setEnd("91a28ced-fc28-30d3-9187-f27f5dc26bef");links.add(link34);
        Link link35= new Link();link35.setStart("1b6e0838-807d-3918-a886-4e44188c28ea");link35.setEnd("91a28ced-fc28-30d3-9187-f27f5dc26bef");links.add(link35);
        Link link36= new Link();link36.setStart("7fbaae65-da7f-3cd5-a686-49a687a04acd");link36.setEnd("91a28ced-fc28-30d3-9187-f27f5dc26bef");links.add(link36);
        Link link37= new Link();link37.setStart("8269fe3b-4a70-3521-a061-52913c2b2a66");link37.setEnd("33c74519-0bfb-3276-8677-c039a8cbc32e");links.add(link37);
        Link link38= new Link();link38.setStart("521ae0b7-2857-39a5-8fad-638cb1d41cfb");link38.setEnd("91a28ced-fc28-30d3-9187-f27f5dc26bef");links.add(link38);
        Link link39= new Link();link39.setStart("458b3c63-382b-3154-ae19-7bea75c6f65e");link39.setEnd("33c74519-0bfb-3276-8677-c039a8cbc32e");links.add(link39);
        Link link40= new Link();link40.setStart("56d6bcba-08b9-3409-a8c1-cfb50c0de390");link40.setEnd("91a28ced-fc28-30d3-9187-f27f5dc26bef");links.add(link40);
        Link link41= new Link();link41.setStart("dfa91d3c-3acf-3087-81e9-96ac2d2b1761");link41.setEnd("91a28ced-fc28-30d3-9187-f27f5dc26bef");links.add(link41);
        Link link42= new Link();link42.setStart("da7e7b2b-23b9-3ead-bfae-c20c05265e75");link42.setEnd("33c74519-0bfb-3276-8677-c039a8cbc32e");links.add(link42);
        Link link43= new Link();link43.setStart("3a6d064a-f32d-3bc6-9c05-001f1a0d2dfa");link43.setEnd("33c74519-0bfb-3276-8677-c039a8cbc32e");links.add(link43);
        Link link44= new Link();link44.setStart("80b9fd05-ee93-373f-8984-0db14cfcec6e");link44.setEnd("33c74519-0bfb-3276-8677-c039a8cbc32e");links.add(link44);
        Link link45= new Link();link45.setStart("e1d2e5f8-fc78-3f75-9786-04d5a51bf36f");link45.setEnd("ed09a671-d73c-3c6f-acd9-531412075009");links.add(link45);
        Link link46= new Link();link46.setStart("a41eefc8-f5ed-3c54-8500-2d3d1b82ccff");link46.setEnd("ed09a671-d73c-3c6f-acd9-531412075009");links.add(link46);
        Link link47= new Link();link47.setStart("8c5a54cf-7449-3e2c-b7f4-13eca2c421d0");link47.setEnd("ed09a671-d73c-3c6f-acd9-531412075009");links.add(link47);
        Link link48= new Link();link48.setStart("72b4eb8a-eea2-3bb7-acba-0c5f281db29c");link48.setEnd("56d6bcba-08b9-3409-a8c1-cfb50c0de390");links.add(link48);
        Link link49= new Link();link49.setStart("72bf6859-0cf6-3f61-b858-dac6a4887e1d");link49.setEnd("ed09a671-d73c-3c6f-acd9-531412075009");links.add(link49);
        Link link50= new Link();link50.setStart("489ead7a-7b02-3ab0-938d-246d9cd8bfc1");link50.setEnd("ed09a671-d73c-3c6f-acd9-531412075009");links.add(link50);
        Link link51= new Link();link51.setStart("28896262-e2de-370c-a254-d29dc98cc89e");link51.setEnd("56d6bcba-08b9-3409-a8c1-cfb50c0de390");links.add(link51);
        Link link52= new Link();link52.setStart("8026d273-b08e-3a28-bf35-c20c108a1318");link52.setEnd("ed09a671-d73c-3c6f-acd9-531412075009");links.add(link52);
        Link link53= new Link();link53.setStart("4c02802a-0531-36ec-8467-e25995c3faea");link53.setEnd("7fbaae65-da7f-3cd5-a686-49a687a04acd");links.add(link53);
        Link link54= new Link();link54.setStart("366e3566-2c0d-3660-99a8-a32053221f60");link54.setEnd("ed09a671-d73c-3c6f-acd9-531412075009");links.add(link54);
        Link link55= new Link();link55.setStart("dc2c3249-c369-3904-9a85-200f5985bcee");link55.setEnd("dfa91d3c-3acf-3087-81e9-96ac2d2b1761");links.add(link55);
        Link link56= new Link();link56.setStart("2b3c880a-ee0a-3ef1-857e-0efe62a5f4ab");link56.setEnd("dfa91d3c-3acf-3087-81e9-96ac2d2b1761");links.add(link56);
        Link link57= new Link();link57.setStart("b0166edf-5d49-33f0-827c-035bb6833317");link57.setEnd("7fbaae65-da7f-3cd5-a686-49a687a04acd");links.add(link57);
        Link link58= new Link();link58.setStart("afec89d5-f116-3dc0-b8f4-4c02a3211f76");link58.setEnd("dfa91d3c-3acf-3087-81e9-96ac2d2b1761");links.add(link58);
        Link link59= new Link();link59.setStart("430a52fd-eb89-30af-92f5-e6eb46df6426");link59.setEnd("56d6bcba-08b9-3409-a8c1-cfb50c0de390");links.add(link59);
        Link link60= new Link();link60.setStart("9e3653f2-9183-3edf-9e64-26400fadef14");link60.setEnd("ed09a671-d73c-3c6f-acd9-531412075009");links.add(link60);
        Link link61= new Link();link61.setStart("ec1eefed-ea95-356a-af04-be492ce39b68");link61.setEnd("80b9fd05-ee93-373f-8984-0db14cfcec6e");links.add(link61);
        Link link62= new Link();link62.setStart("026ef9ac-07b9-34a9-a5e5-d88cb1567700");link62.setEnd("dfa91d3c-3acf-3087-81e9-96ac2d2b1761");links.add(link62);
        Link link63= new Link();link63.setStart("c0eddb1a-2f3e-39af-a31d-46a8f4f6accb");link63.setEnd("ed09a671-d73c-3c6f-acd9-531412075009");links.add(link63);
        Link link64= new Link();link64.setStart("51d73da4-def0-3462-8082-83e7d6134ab5");link64.setEnd("80b9fd05-ee93-373f-8984-0db14cfcec6e");links.add(link64);
        Link link65= new Link();link65.setStart("3cfb1a56-2d62-38e3-bdeb-322fac15e3c8");link65.setEnd("ed09a671-d73c-3c6f-acd9-531412075009");links.add(link65);
        Link link66= new Link();link66.setStart("b587a9c9-fdb2-3c9b-a3a2-4e1f622a63aa");link66.setEnd("ed09a671-d73c-3c6f-acd9-531412075009");links.add(link66);
        Link link67= new Link();link67.setStart("bf74e2b9-bdd9-3d76-ba03-06dcdfe88487");link67.setEnd("56d6bcba-08b9-3409-a8c1-cfb50c0de390");links.add(link67);
        Link link68= new Link();link68.setStart("e58db581-d127-3737-a6cf-e50ad6726871");link68.setEnd("56d6bcba-08b9-3409-a8c1-cfb50c0de390");links.add(link68);
        Link link69= new Link();link69.setStart("d8fbb4be-9b52-3f57-811b-1e17e8208573");link69.setEnd("7fbaae65-da7f-3cd5-a686-49a687a04acd");links.add(link69);
        Link link70= new Link();link70.setStart("854acb99-d7e0-387f-9b17-4c57da03278a");link70.setEnd("ed09a671-d73c-3c6f-acd9-531412075009");links.add(link70);
        Link link71= new Link();link71.setStart("d07ddcf5-4d6e-3689-81d0-0c194e30344a");link71.setEnd("dfa91d3c-3acf-3087-81e9-96ac2d2b1761");links.add(link71);
        Link link72= new Link();link72.setStart("98aab18c-5fd5-3069-ab6a-76fd51c5988c");link72.setEnd("56d6bcba-08b9-3409-a8c1-cfb50c0de390");links.add(link72);
        Link link73= new Link();link73.setStart("dc44a71d-3ef6-3a78-8331-bf429978b537");link73.setEnd("ed09a671-d73c-3c6f-acd9-531412075009");links.add(link73);
        Link link74= new Link();link74.setStart("675e99ec-1120-38b3-a194-ee42146c96fe");link74.setEnd("ed09a671-d73c-3c6f-acd9-531412075009");links.add(link74);
        Link link75= new Link();link75.setStart("6f462f38-fd88-3735-bc0a-58f02d44ec29");link75.setEnd("ed09a671-d73c-3c6f-acd9-531412075009");links.add(link75);
        Link link76= new Link();link76.setStart("cf9063f2-cc47-3e8e-a3c0-6eea65c81eb8");link76.setEnd("ed09a671-d73c-3c6f-acd9-531412075009");links.add(link76);
        Link link77= new Link();link77.setStart("5d5e3194-d2b3-3663-b5a5-9dd222270cc9");link77.setEnd("ed09a671-d73c-3c6f-acd9-531412075009");links.add(link77);
        Link link78= new Link();link78.setStart("1e54fb7b-6789-35ae-9d94-bf5a564c13dc");link78.setEnd("80b9fd05-ee93-373f-8984-0db14cfcec6e");links.add(link78);
        Link link79= new Link();link79.setStart("a9a3f396-ff0a-3873-bb65-3f2450cac02b");link79.setEnd("56d6bcba-08b9-3409-a8c1-cfb50c0de390");links.add(link79);
        Link link80= new Link();link80.setStart("e829552a-988d-34cb-b5b0-69ad53385a0b");link80.setEnd("56d6bcba-08b9-3409-a8c1-cfb50c0de390");links.add(link80);
        Link link81= new Link();link81.setStart("47aca154-fbb2-3762-8bd2-b3f524026513");link81.setEnd("80b9fd05-ee93-373f-8984-0db14cfcec6e");links.add(link81);
        Link link82= new Link();link82.setStart("06d5a52c-daf1-3905-b579-7eac2ec17121");link82.setEnd("ed09a671-d73c-3c6f-acd9-531412075009");links.add(link82);
        Link link83= new Link();link83.setStart("7808e653-d61f-3ec8-8ac3-934ed32078ee");link83.setEnd("ed09a671-d73c-3c6f-acd9-531412075009");links.add(link83);
        Link link84= new Link();link84.setStart("d4e937df-6509-3a2e-bc8d-ad0147f86c4c");link84.setEnd("ed09a671-d73c-3c6f-acd9-531412075009");links.add(link84);
        Link link85= new Link();link85.setStart("f9b48348-6e88-3ebb-b930-de4eeb3b7fb8");link85.setEnd("56d6bcba-08b9-3409-a8c1-cfb50c0de390");links.add(link85);
        Link link86= new Link();link86.setStart("742c38f7-6fd9-388c-b817-72160154b4ac");link86.setEnd("56d6bcba-08b9-3409-a8c1-cfb50c0de390");links.add(link86);
        Link link87= new Link();link87.setStart("83f25633-e74c-3f7b-ba0d-e7b69932dc13");link87.setEnd("ed09a671-d73c-3c6f-acd9-531412075009");links.add(link87);
        Link link88= new Link();link88.setStart("7867f16e-0d0c-3215-a9e3-8a9cc9ad1b1e");link88.setEnd("ed09a671-d73c-3c6f-acd9-531412075009");links.add(link88);
        Link link89= new Link();link89.setStart("773b1560-c269-3eac-8f14-8305c62ff9ae");link89.setEnd("7fbaae65-da7f-3cd5-a686-49a687a04acd");links.add(link89);
        Link link90= new Link();link90.setStart("aba43f75-328e-3e64-bca6-d1421b4da982");link90.setEnd("dfa91d3c-3acf-3087-81e9-96ac2d2b1761");links.add(link90);
        Link link91= new Link();link91.setStart("3e45b3b5-f359-3727-8368-568091a5f313");link91.setEnd("dfa91d3c-3acf-3087-81e9-96ac2d2b1761");links.add(link91);
        Link link92= new Link();link92.setStart("2762ba27-c971-35f4-81a2-21847537a4ca");link92.setEnd("ed09a671-d73c-3c6f-acd9-531412075009");links.add(link92);
        Link link93= new Link();link93.setStart("7dc7c729-c1b8-382d-930b-23d0b28a684b");link93.setEnd("56d6bcba-08b9-3409-a8c1-cfb50c0de390");links.add(link93);
        Link link94= new Link();link94.setStart("260b759a-5b92-393f-a011-15b3c26400d2");link94.setEnd("56d6bcba-08b9-3409-a8c1-cfb50c0de390");links.add(link94);
        Link link95= new Link();link95.setStart("d5f485f1-de27-3c15-aca6-dff395983ea0");link95.setEnd("dfa91d3c-3acf-3087-81e9-96ac2d2b1761");links.add(link95);
        Link link96= new Link();link96.setStart("c80a0670-954b-3390-9e00-2a0a491a7a42");link96.setEnd("80b9fd05-ee93-373f-8984-0db14cfcec6e");links.add(link96);
        Link link97= new Link();link97.setStart("bb9c93f0-5b57-3869-8e00-73205dd3d738");link97.setEnd("ed09a671-d73c-3c6f-acd9-531412075009");links.add(link97);
        Link link98= new Link();link98.setStart("4b1d59c8-22f4-39d0-90f9-eb1e278a6811");link98.setEnd("ed09a671-d73c-3c6f-acd9-531412075009");links.add(link98);
        Link link99= new Link();link99.setStart("4a0578fa-ec61-3d85-a254-ddf18c07937c");link99.setEnd("ed09a671-d73c-3c6f-acd9-531412075009");links.add(link99);


    }

}

class Link {
    String start;
    String end;

    /**
     * @return start - {return content description}
     */
    public String getStart() {
        return start;
    }

    /**
     * @param start - {parameter description}.
     */
    public void setStart(final String start) {
        this.start = start;
    }

    /**
     * @return end - {return content description}
     */
    public String getEnd() {
        return end;
    }

    /**
     * @param end - {parameter description}.
     */
    public void setEnd(final String end) {
        this.end = end;
    }
}
