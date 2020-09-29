package agri_asan.com.agriasan06_12_19;


public class QuestionsForDomainDataModel {

    String question;
    String answer;
    String question_city;
    String answer_city;
    String question_time;
    String answer_time;
    String occupation;
    String fasal;
    String question_phone;
    String answer_phone;
    String ID;
    String question_name;
    String answer_name;

    public QuestionsForDomainDataModel(String question, String answer, String question_city, String answer_city, String question_time, String answer_time,
                                       String occupation, String fasal, String question_phone, String answer_phone, String ID, String question_name, String answer_name,
                                       String name, String type, String version_number, String feature) {
        this.question = question;
        this.answer = answer;
        this.question_city = question_city;
        this.answer_city = answer_city;
        this.question_time = question_time;
        this.answer_time = answer_time;
        this.occupation = occupation;
        this.fasal = fasal;
        this.question_phone = question_phone;
        this.answer_phone = answer_phone;
        this.ID = ID;
        this.question_name = question_name;
        this.answer_name = answer_name;
        this.name = name;
        this.type = type;
        this.version_number = version_number;
        this.feature = feature;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion_city() {
        return question_city;
    }

    public void setQuestion_city(String question_city) {
        this.question_city = question_city;
    }

    public String getAnswer_city() {
        return answer_city;
    }

    public void setAnswer_city(String answer_city) {
        this.answer_city = answer_city;
    }

    public String getQuestion_time() {
        return question_time;
    }

    public void setQuestion_time(String question_time) {
        this.question_time = question_time;
    }

    public String getAnswer_time() {
        return answer_time;
    }

    public void setAnswer_time(String answer_time) {
        this.answer_time = answer_time;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getFasal() {
        return fasal;
    }

    public void setFasal(String fasal) {
        this.fasal = fasal;
    }

    public String getQuestion_phone() {
        return question_phone;
    }

    public void setQuestion_phone(String question_phone) {
        this.question_phone = question_phone;
    }

    public String getAnswer_phone() {
        return answer_phone;
    }

    public void setAnswer_phone(String answer_phone) {
        this.answer_phone = answer_phone;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getQuestion_name() {
        return question_name;
    }

    public void setQuestion_name(String question_name) {
        this.question_name = question_name;
    }

    public String getAnswer_name() {
        return answer_name;
    }

    public void setAnswer_name(String answer_name) {
        this.answer_name = answer_name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setVersion_number(String version_number) {
        this.version_number = version_number;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    String name;
    String type;
    String version_number;
    String feature;

    public QuestionsForDomainDataModel(String name, String type, String version_number, String feature ) {
        this.name=name;
        this.type=type;
        this.version_number=version_number;
        this.feature=feature;

    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getVersion_number() {
        return version_number;
    }

    public String getFeature() {
        return feature;
    }

}

