package Neo4j;

import Entity.Neighbour;
import Entity.Relation;
import org.junit.Test;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class Neo4jAPITest {

    @Test
    public void relationsByType() {
        Neo4jAPI neo4j = new Neo4jAPI();

        String neoUser = "neo4j";
        String neoPassword = "3203";
        String neoHost = "localhost";
        String boltPort = "7687";
        String uri = "bolt://" + neoHost + ":" + boltPort;
        Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(neoUser, neoPassword));
        Session session = driver.session();

        String[] type = {"导致", "或", "是一种", "条件为", "与", "并发症", "调查病史", "否定修饰", "然后", "同义词",
                "修饰限定", "强导致", "表现为", "弱导致", "作用于", "等价", "定义为", "执行检查", "检查发现", "转变为", "比例为"};

        for (int i = 0; i<type.length;i++) {
            LocalDateTime start = LocalDateTime.now();

            Relation[] test = neo4j.relationsByType(type[i], session);

            LocalDateTime end = LocalDateTime.now();

            String duration = Duration.between(start, end).toString();

            System.out.print(type[i] + "\t" + duration.substring(2, duration.length()-1) + "\n");
        }

        driver.close();
    }

    @Test
    public void entitiesByType() {
        Neo4jAPI neo4j = new Neo4jAPI();

        String neoUser = "neo4j";
        String neoPassword = "3203";
        String neoHost = "localhost";
        String boltPort = "7687";
        String uri = "bolt://" + neoHost + ":" + boltPort;
        Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(neoUser, neoPassword));
        Session session = driver.session();

        String[] type = {"疾病", "嵌套", "症状", "病理概念", "阶段", "否定词", "程度修饰语", "化学物质", "事件", "时间",
                "状态修饰语", "部位", "人群分类", "生物", "病史", "频率修饰语", "年龄", "修饰语", "性别", "生理概念",
                "性质修饰语", "数据", "病症", "颜色", "食品", "检查", "数量修饰语", "方位修饰语", "其他"};

        for (int i = 0; i<type.length;i++) {
            LocalDateTime start = LocalDateTime.now();

            String[] test = neo4j.entitiesByType(type[i], session);

            LocalDateTime end = LocalDateTime.now();

            String duration = Duration.between(start, end).toString();

            System.out.print(type[i] + "\t" + duration.substring(2, duration.length()-1) + "\n");
        }

        driver.close();
    }

    @Test
    public void downwardRecursion() {
        Neo4jAPI neo4j = new Neo4jAPI();

        String neoUser = "neo4j";
        String neoPassword = "3203";
        String neoHost = "localhost";
        String boltPort = "7687";
        String uri = "bolt://" + neoHost + ":" + boltPort;
        Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(neoUser, neoPassword));
        Session session = driver.session();

        String[] entityName = {"overactive bladder","慢性萎缩性胃炎或胃淀粉样变或胰腺炎或胆囊炎","避孕药","主动脉弓综合征","病毒或细菌或真菌或原虫或蠕虫感染导致肠炎或急性出血性坏死性肠炎","肺炎或肺脓肿或肺结核或胸膜炎","连续阵发性剧咳与高调吸气回声","瘢痕","高血压导致肾炎性水肿","低钾血症",
                "心室肥大","白果或有机磷或阿托品或樟脑","多囊卵巢综合征","呼气","重度增生性和（或）膜性狼疮性肾炎导致大量蛋白尿","病毒性肝炎或胆道感染或胆道阻塞或继发性或原发肝癌","获得性肾囊肿","高血糖或高血压","感冒","胃泌素",
                "营养不良或脑发育不全","反食定义为无恶心呕吐","大便带血","变态反应性肠炎或过敏性紫癜或药物或内分泌疾病","H血清型","无红细胞","稳定不体循环功能或肾血流减少或心功能不全或心律失常严重","软化性骨质","急性心肌梗死","膜增生性肾小球肾炎导致急性肾炎综合征",
                "体温调节障碍","放射线照射","便血定义为消化道出血","支气管结石或支气管腺瘤或支气管黏膜非特异性溃疡","头晕或眩晕或恶心或上腹不适或面色苍白","囊肿破裂出血或结石或血块导致尿路梗阻","1型糖尿病条件为Ⅳ期","功能性蛋白尿或体位性蛋白尿","感觉错乱","肺泡炎",
                "判断力降低","糊状或水样便或脓血便","肾小管损害","神经焦虑或非细菌感染导致尿道综合征","γ‐氨基丁酸同义词GABA","干呕同义词vomiturition","水痘-带状疱疹病毒或EB病毒或流感病毒或感染条件为感染极期或3～5天","重症肝炎或流行性出血热或白血病或过敏性紫癜","早期晨间起床","血栓性",
                "继发性","细菌或结核菌或真菌或病毒或支原体或寄生虫肺部感染或肺部肿瘤","轻度贫血","慢性肾炎","解热镇痛药或磺胺类","糖尿病性骨病或皮质醇增多症性骨病或甲状腺或甲状旁腺或代谢障碍性脂蛋白关节炎或痛风","血尿与剧烈腰痛或腹痛与寒战与高热","间脑综合征","肾盂肾盏或输尿管或膀胱或前列腺病变","慢而深",
                "传入冲动","铅是一种重金属","尿毒症肺水肿","淋巴代谢失调","细胞呼吸作用","急性细菌性痢疾或伤寒或副伤寒或肠结核","无症状性","支气管黏膜腺体或杯状细胞","多次分娩或产伤","急性出血坏死性胰腺炎",
                "毛细血管持久收缩","患重病","糖尿病酮症酸中毒或吗啡类药物中毒或有机磷杀虫药中毒","出血量多与胃停留时间短与食管出血","心脏工作量增加","hyperplasia of prostate","血清碱性磷酸酶","睡眠中易惊醒","肾小管增加钠水重吸收","神经系统疾病导致消瘦",
                "常染色体显性多囊肾病是一种遗传性肾脏疾病","急性肾小球肾炎或急进性肾小球肾炎","高温中暑","微小病变型肾病或轻度系膜增生性肾小球肾炎","持续","疼痛加重条件为活动过多","胆总管结石或狭窄或炎性水肿或肿瘤或蛔虫导致肝外性胆汁淤积","弹性降低","结石病史或肿瘤病史","焦虑或紧张或情绪激动",
                "活动腰部","肾小球性血尿","病房男女同室","网织红细胞增加与骨髓红细胞系列增生旺盛","食欲下降","高温环境导致水肿","性情急躁","遗传性或获得性近端肾小管复合性功能缺陷疾病","纵隔肿瘤及脓肿或左心房肥大","悲伤或痛苦难熬或愁眉苦脸或唉声叹气"};

        for (int i = 0; i<entityName.length;i++) {
            LocalDateTime start = LocalDateTime.now();

            neo4j.downwardRecursion(entityName[i], session);

            LocalDateTime end = LocalDateTime.now();

            String duration = Duration.between(start, end).toString();

            System.out.print(entityName[i] + "\t" + duration.substring(2, duration.length()-1) + "\n");
        }

        driver.close();
    }

    @Test
    public void upwardRecursion() {
        Neo4jAPI neo4j = new Neo4jAPI();
        String neoUser = "neo4j";
        String neoPassword = "3203";
        String neoHost = "localhost";
        String boltPort = "7687";
        String uri = "bolt://" + neoHost + ":" + boltPort;
        Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(neoUser, neoPassword));
        Session session = driver.session();

        String[] id = {"8c09d2d472f711ea80fb9eb6d0c289e1","8f95d3f072f611ea95959eb6d0c289e1","8d10dd8272f211ea93c39eb6d0c289e1","34b8846872f611ea80c29eb6d0c289e1","0f68713672f611eab67a9eb6d0c289e1","9a2cf0a872f611eabeea9eb6d0c289e1","0899e77472f711eaba7f9eb6d0c289e1","466f968272f611eabd819eb6d0c289e1","075a5cda72f711eaa0969eb6d0c289e1","c6e5770a72f611eab1db9eb6d0c289e1",
                "3ceb772e72f711eaadc89eb6d0c289e1","432d7cc872f311ea85f29eb6d0c289e1","32ff097072f511eabdd49eb6d0c289e1","0072dd0672f711eab4059eb6d0c289e1","e3f4daf672f711eab7d39eb6d0c289e1","9ab92ae872f611ea93839eb6d0c289e1","259bfc9a72f311eaa0dd9eb6d0c289e1","08a43e4872f711ea9a1b9eb6d0c289e1","c9d5071a72f311eab5d49eb6d0c289e1","09d5682c72f611eaa2ee9eb6d0c289e1",
                "d05dafa472f611eabb9b9eb6d0c289e1","0c17d1ac72f211ea9ace9eb6d0c289e1","4812cf2c72f711eaa7ee9eb6d0c289e1","35b68e2872f511ea91fc9eb6d0c289e1","2acdb27872f311ea93439eb6d0c289e1","141df1e272f511eaac589eb6d0c289e1","3e2e7dd872f711eaa2db9eb6d0c289e1","8d822d3672f211ea95a59eb6d0c289e1","3b59fc4672f511eabf709eb6d0c289e1","187b08c672f411ea84eb9eb6d0c289e1",
                "9bde9a3672f711ea9ebb9eb6d0c289e1","816f185e72f611eaaf999eb6d0c289e1","8e57100072f311ea81119eb6d0c289e1","43d6088c72f311ea9a329eb6d0c289e1","7608703e72f211ea9ca99eb6d0c289e1","1a56373a72f511ea9b279eb6d0c289e1","976b87d872f711eabd729eb6d0c289e1","d9760db872f511ea94399eb6d0c289e1","bf492e1c72f511ea8b209eb6d0c289e1","0961bb4c72f211ea86f59eb6d0c289e1",
                "77741db072f211eaa2f69eb6d0c289e1","844c37c872f211eab8fa9eb6d0c289e1","8650d0b872f211ea80049eb6d0c289e1","847912a872f411eaa2529eb6d0c289e1","9183acdc72f611eabfc89eb6d0c289e1","7191ee0272f211ea873d9eb6d0c289e1","decb58dc72f411ea8e279eb6d0c289e1","c410cfac72f511ea884f9eb6d0c289e1","80dba72672f711eababa9eb6d0c289e1","477861f672f611eab9339eb6d0c289e1",
                "7bae1cfa72f711eaa1089eb6d0c289e1","865f830072f211eaaeae9eb6d0c289e1","38296c2e72f311eab3119eb6d0c289e1","4b8d40b472f611eaa6069eb6d0c289e1","742f753472f211eab8369eb6d0c289e1","3ba725cc72f511eab8479eb6d0c289e1","81237f1c72f711ea83859eb6d0c289e1","88bf33e472f311eaa59d9eb6d0c289e1","c365f5e272f511ea84d59eb6d0c289e1","c542ed9e72f311ea9eb69eb6d0c289e1",
                "e8027d2672f411eab7e89eb6d0c289e1","e4a25fc272f711eaaaf59eb6d0c289e1","3268867872f511eab9f09eb6d0c289e1","339bdd3472f311eab7229eb6d0c289e1","4c52c74272f711ea8f809eb6d0c289e1","dbe74e4672f311eaa6d29eb6d0c289e1","c683254072f311ea87159eb6d0c289e1","2c4fb35872f311ea9f069eb6d0c289e1","e718edac72f411eaa6b09eb6d0c289e1","09499b3672f611eaa4149eb6d0c289e1",
                "d9fedbba72f311eaa41d9eb6d0c289e1","7eb89f4072f711eaacb39eb6d0c289e1","c274bebe72f311eab5919eb6d0c289e1","8b6516f472f711eaa2eb9eb6d0c289e1","8110b31872f511eab2ec9eb6d0c289e1","8a07d7e672f311ea8da39eb6d0c289e1","92a5a2d872f611ea8e6c9eb6d0c289e1","205c9d7872f411eaa7dd9eb6d0c289e1","45a270da72f711eaa9459eb6d0c289e1","cde8751272f311eab6579eb6d0c289e1",
                "4381ec0072f311eaa9249eb6d0c289e1","e4cee20872f411ea92249eb6d0c289e1","c593d9b472f511ea91ef9eb6d0c289e1","d5ba5d1a72f311ea9a1c9eb6d0c289e1","6ed0cff672f411eab4889eb6d0c289e1","731e737672f211ea9cce9eb6d0c289e1","3462fe9a72f311ea8b399eb6d0c289e1","7911590072f711eaa9ab9eb6d0c289e1","ff720e2672f511ea96239eb6d0c289e1","32c080c672f311eab4359eb6d0c289e1",
                "087be89e72f411eab3aa9eb6d0c289e1","465ea19872f611ea8f2d9eb6d0c289e1","43182c7e72f611eaac329eb6d0c289e1","0f6df44c72f711eaa4179eb6d0c289e1","c0fc613472f511eaa0d89eb6d0c289e1","37a431e872f611ea80f19eb6d0c289e1","7b90698272f711ea93f19eb6d0c289e1","8128bd8672f611eab9469eb6d0c289e1","16c4bade72f411ea9bac9eb6d0c289e1","7186144272f411eaaad39eb6d0c289e1"};

        for (int i = 0; i<id.length;i++) {
            LocalDateTime start = LocalDateTime.now();

            neo4j.upwardRecursion(id[i], session);

            LocalDateTime end = LocalDateTime.now();

            String duration = Duration.between(start, end).toString();

            System.out.print(id[i] + "\t" + duration.substring(2, duration.length()-1) + "\n");
        }

        driver.close();
    }

    @Test
    public void neighbours() {
        Neo4jAPI neo4j = new Neo4jAPI();

        String neoUser = "neo4j";
        String neoPassword = "3203";
        String neoHost = "localhost";
        String boltPort = "7687";
        String uri = "bolt://" + neoHost + ":" + boltPort;
        Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(neoUser, neoPassword));
        Session session = driver.session();

        String[] entityName = {"过度通气综合征或痛风","口腔或食管或胃肠或肝或胆或胰疾病是一种消化系统疾病","左腹部或下腹痉挛性疼痛与下坠感条件为排便","流行性出血热或钩端螺旋体病或登革热","育龄妇女月经来潮前7～14天","有一段间歇","生气或情绪激动或不良刺激","尿毒症或肝性脑病是一种中毒","进油腻食物史导致胆囊炎或胆石症","低比重",
                "3～5天","直接或间接暴力","滤膜通透性","生活环境改变与精神紧张","肾功能的进行性减退","炎","皮肤和黏膜干燥与低血压","夹层动脉瘤","右心或体循环静脉栓子脱落导致肺栓塞","维生素B1缺乏",
                "钩端螺旋体病","钝痛或胀痛","反食是一种反流症状","急进性肾小球肾炎导致尿毒症","肉眼血尿或镜下血尿","药物副作用","弥漫增殖性","流行性乙型脑炎或斑疹伤寒或流行性脑脊髓膜炎或中毒性菌痢或中暑","γ‐氨基丁酸受体","疼痛明显条件为弯腰",
                "多发性神经根炎","老年性紫癜","尿毒症脑病或内分泌紊乱或细胞免疫功能低下并发症慢性肾衰竭","咳嗽与呼吸困难","Zollinger‐Ellison综合征","尿路结石或肿瘤","膀胱颈以下","去甲肾上腺素功能失调","膀胱颈部病变或后尿道疾患或前尿道疾患","一过性肾功能不全",
                "血液稀释","尿内多糖","增生性关节炎","午后低热","食管或纵隔病变导致胸骨后胸痛","颈段食管蹼","高血压脑病或脑血管意外","近端肾小管酸中毒是一种AG正常的高血氯性代谢性酸中毒","出血性坏死性肠炎","下腹部手术",
                "口腔或食管或胃肠或肝或胆或胰疾病","疲乏或失眠或头晕","神经感觉纤维","尿频与多饮与多尿与口渴","狭窄修补术","微小病变型肾病或轻度系膜增生性肾小球肾炎导致肾病综合征","正常的","全身性抽搐表现为骨骼肌痉挛与多意识丧失","自主神经‐内分泌功能障碍","钠水重吸收增加",
                "慢性消耗性疾病导致营养物质消耗增加","蛋白尿或大量蛋白尿或肾病综合征与糖尿病眼底病变","左腹部或下腹痉挛性疼痛与下坠感","眼或耳或鼻或鼻窦或牙齿病变","持续性镜下血尿","高血压或低血压或心律失常或病态窦房结综合征或心脏瓣膜病或心肌缺血或颈动脉窦综合征","精神异常","肛门或直肠疾病","摇晃感","分泌皮质醇过多",
                "系统性红斑狼疮或结节性多动脉炎或皮肌炎或类风湿关节炎","神经系统","暗红色尿","病毒或细菌","膀胱充盈条件为短时间与膀胱膨胀与下腹胀痛与膨隆与尿意急迫","血尿与蛋白尿与管型尿与无菌性白细胞尿与肾功能进行性恶化","超高热定义为41℃以上","结肠良或恶性肿瘤或Crohn病或先天性巨结肠","黑色或鲜红色或暗红色","支气管肺癌导致咯血大",
                "药物敏感","1型糖尿病条件为Ⅳ期导致高血压加重与糖尿病与微血管并发症","X连锁显性遗传或常染色体隐性遗传或常染色体显性遗传","无尿完全","狼疮性肾炎或过敏性紫癜肾炎或Alport综合征条件为早期或薄基底膜肾病或非典型的急性肾炎条件为恢复期","吸入刺激性气体或异物或淋巴结或肿瘤作用于气管或支气管分叉处","胆红素代谢功能削弱","垂体性尿崩症导致内分泌代谢障碍","水分渗入","畏寒少汗",
                "肾小球硬化或肾小管萎缩或肾间质纤维化","口腔炎或咽后壁脓肿","黏膜血点","红或肿","后尿道疾患","快","轻微出血","肌肉收缩性头痛","吸气性","肾脏内分泌功能紊乱与糖耐量异常和胰岛素抵抗与下丘脑-垂体内分泌功能紊乱"};

        for (int i = 0; i<entityName.length;i++) {
            LocalDateTime start = LocalDateTime.now();

            Neighbour[] test = neo4j.neighbours("体温", session);

            LocalDateTime end = LocalDateTime.now();

            String duration = Duration.between(start, end).toString();

            System.out.print(entityName[i] + "\t" + duration.substring(2, duration.length()-1) + "\n");
        }

        driver.close();
    }
}