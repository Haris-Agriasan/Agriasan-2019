package agri_asan.com.agriasan06_12_19;


import java.util.ArrayList;

public class DataModel {

    private String Views;

    private String Customer_Type;
    private String Order_Date;
    private String Order_ID;
    private String Order_Status;
    private String Order_Time;
    private String Price_Total;
    private String Product_Quantity;
    private String Product_Vendor_Phone;
    private String Product_Vendor_Type;
    private String Receiver_Address;
    private String Receiver_Name;
    private String Receiver_Phone;

    public DataModel(String customer_Type, String order_Date, String order_Status,
                     String order_Time, String price_Total, String product_ID,
                     String product_Quantity, String product_Vendor_Type,
                     String receiver_Address, String receiver_Name, String receiver_Phone, String order_id,

                     String product_Name, String product_Price, String product_Detail, String vendor_Contact_No,
                     String product_Date, String product_Time, String product_image_0,
                     String Product_ID, String product_measure_in,
                     String product_litre_kilo,String no_images ,
                     ArrayList<String> images_list, String verification,
                     String user_type, String cart, String cart_product_quantity, String member_id){
        Customer_Type=customer_Type;
        Order_Date=order_Date;
        Order_Status=order_Status;
        Order_Time=order_Time;
        Price_Total=price_Total;
        Product_ID=product_ID;
        Product_Quantity=product_Quantity;
        Product_Vendor_Type=product_Vendor_Type;
        Receiver_Address=receiver_Address;
        Receiver_Name=receiver_Name;
        Receiver_Phone=receiver_Phone;

        Product_Name = product_Name;
        Product_Price = product_Price;
        Product_Detail = product_Detail;
        Product_Date = product_Date;
        Product_Time = product_Time;
        Product_Image_0=product_image_0;
        this.Product_ID=Product_ID;
        Product_Measure_In=product_measure_in;
        Product_Litre_Kilo=product_litre_kilo;
        Images_List=images_list;
        Product_No_Images=no_images;
        Verification =verification;
        this.user_type=user_type;
        this.Vendor_Contact_No=vendor_Contact_No;
        this.Cart=cart;
        this.Cart_product_quantity=cart_product_quantity;
        this.member_id=member_id;
        this.Order_ID=order_id;
    }

    public DataModel(String customer_Type, String order_Date, String order_Status,
                     String order_Time, String price_Total, String product_ID,
                     String product_Quantity, String product_Vendor_Type,
                     String receiver_Address, String receiver_Name, String receiver_Phone,

                     String product_Name, String product_Price, String product_Detail, String vendor_Contact_No,
                     String product_Date, String product_Time, String product_image_0,
                     String Product_ID, String product_measure_in,
                     String product_litre_kilo,String no_images ,
                     ArrayList<String> images_list, String verification,
                     String user_type, String cart, String cart_product_quantity, String member_id){
        Customer_Type=customer_Type;
        Order_Date=order_Date;
        Order_Status=order_Status;
        Order_Time=order_Time;
        Price_Total=price_Total;
        Product_ID=product_ID;
        Product_Quantity=product_Quantity;
        Product_Vendor_Type=product_Vendor_Type;
        Receiver_Address=receiver_Address;
        Receiver_Name=receiver_Name;
        Receiver_Phone=receiver_Phone;

        Product_Name = product_Name;
        Product_Price = product_Price;
        Product_Detail = product_Detail;
        Product_Date = product_Date;
        Product_Time = product_Time;
        Product_Image_0=product_image_0;
        this.Product_ID=Product_ID;
        Product_Measure_In=product_measure_in;
        Product_Litre_Kilo=product_litre_kilo;
        Images_List=images_list;
        Product_No_Images=no_images;
        Verification =verification;
        this.user_type=user_type;
        this.Vendor_Contact_No=vendor_Contact_No;
        this.Cart=cart;
        this.Cart_product_quantity=cart_product_quantity;
        this.member_id=member_id;
    }


    public String getViews() {
        return Views;
    }

    public void setViews(String views) {
        Views = views;
    }

    public String getCustomer_Type() {
        return Customer_Type;
    }

    public void setCustomer_Type(String customer_Type) {
        Customer_Type = customer_Type;
    }

    public String getOrder_Date() {
        return Order_Date;
    }

    public void setOrder_Date(String order_Date) {
        Order_Date = order_Date;
    }

    public String getOrder_ID() {
        return Order_ID;
    }

    public void setOrder_ID(String order_ID) {
        Order_ID = order_ID;
    }

    public String getOrder_Status() {
        return Order_Status;
    }

    public void setOrder_Status(String order_Status) {
        Order_Status = order_Status;
    }

    public String getOrder_Time() {
        return Order_Time;
    }

    public void setOrder_Time(String order_Time) {
        Order_Time = order_Time;
    }

    public String getPrice_Total() {
        return Price_Total;
    }

    public void setPrice_Total(String price_Total) {
        Price_Total = price_Total;
    }


    public String getProduct_Quantity() {
        return Product_Quantity;
    }

    public void setProduct_Quantity(String product_Quantity) {
        Product_Quantity = product_Quantity;
    }

    public String getProduct_Vendor_Phone() {
        return Product_Vendor_Phone;
    }

    public void setProduct_Vendor_Phone(String product_Vendor_Phone) {
        Product_Vendor_Phone = product_Vendor_Phone;
    }

    public String getProduct_Vendor_Type() {
        return Product_Vendor_Type;
    }

    public void setProduct_Vendor_Type(String product_Vendor_Type) {
        Product_Vendor_Type = product_Vendor_Type;
    }

    public String getReceiver_Address() {
        return Receiver_Address;
    }

    public void setReceiver_Address(String receiver_Address) {
        Receiver_Address = receiver_Address;
    }

    public String getReceiver_Name() {
        return Receiver_Name;
    }

    public void setReceiver_Name(String receiver_Name) {
        Receiver_Name = receiver_Name;
    }

    public String getReceiver_Phone() {
        return Receiver_Phone;
    }

    public void setReceiver_Phone(String receiver_Phone) {
        Receiver_Phone = receiver_Phone;
    }

    //////////////////////////////////////

    String Product_Type;

    String Vendor_Contact_No;

    String Product_Name;
    String Product_Price;
    String Product_Detail;
    String Product_Date;
    String Product_Time;
    String Product_Image_0;
    String Product_ID;
    String Product_Measure_In;
    String Product_Litre_Kilo;
    ArrayList<String> Images_List;
    String Product_No_Images;
    String Verification;
    String user_type;

    String Cart;
    String Cart_product_quantity;
    public DataModel(String product_Name, String product_Price, String product_Detail, String vendor_Contact_No,
                     String product_Date, String product_Time, String product_image_0,
                     String Product_ID, String product_measure_in,
                     String product_litre_kilo,String no_images ,
                     ArrayList<String> images_list, String verification, String user_type  ) {
        Product_Name = product_Name;
        Product_Price = product_Price;
        Product_Detail = product_Detail;
        Product_Date = product_Date;
        Product_Time = product_Time;
        Product_Image_0=product_image_0;
        this.Product_ID=Product_ID;
        Product_Measure_In=product_measure_in;
        Product_Litre_Kilo=product_litre_kilo;
        Images_List=images_list;
        Product_No_Images=no_images;
        Verification =verification;
        this.user_type=user_type;
        this.Vendor_Contact_No=vendor_Contact_No;


    }
    public DataModel(String product_Name, String product_Price, String product_Detail, String vendor_Contact_No,
                     String product_Date, String product_Time, String product_image_0,
                     String Product_ID, String product_measure_in,
                     String product_litre_kilo,String no_images ,
                     ArrayList<String> images_list, String verification, String user_type ,String product_Vendor_Type ) {
        Product_Name = product_Name;
        Product_Price = product_Price;
        Product_Detail = product_Detail;
        Product_Date = product_Date;
        Product_Time = product_Time;
        Product_Image_0=product_image_0;
        this.Product_ID=Product_ID;
        Product_Measure_In=product_measure_in;
        Product_Litre_Kilo=product_litre_kilo;
        Images_List=images_list;
        Product_No_Images=no_images;
        Verification =verification;
        this.user_type=user_type;
        this.Vendor_Contact_No=vendor_Contact_No;
        this.Product_Vendor_Type=product_Vendor_Type;


    }

    public DataModel(String product_Name, String product_Price, String product_Detail, String vendor_Contact_No,
                     String product_Date, String product_Time, String product_image_0,
                     String Product_ID, String product_measure_in,
                     String product_litre_kilo,String no_images ,
                     ArrayList<String> images_list, String verification,
                     String user_type, String cart, String cart_product_quantity, String member_id) {
        Product_Name = product_Name;
        Product_Price = product_Price;
        Product_Detail = product_Detail;
        Product_Date = product_Date;
        Product_Time = product_Time;
        Product_Image_0=product_image_0;
        this.Product_ID=Product_ID;
        Product_Measure_In=product_measure_in;
        Product_Litre_Kilo=product_litre_kilo;
        Images_List=images_list;
        Product_No_Images=no_images;
        Verification =verification;
        this.user_type=user_type;
        this.Vendor_Contact_No=vendor_Contact_No;
        this.Cart=cart;
        this.Cart_product_quantity=cart_product_quantity;
        this.member_id=member_id;


    }
    public DataModel(String product_Name, String product_Price, String product_Detail, String vendor_Contact_No,
                     String product_Date, String product_Time, String product_image_0,
                     String Product_ID, String product_measure_in,
                     String product_litre_kilo,String no_images ,
                     ArrayList<String> images_list, String verification,
                     String user_type, String cart, String cart_product_quantity, String member_id, String product_Vendor_Type) {
        Product_Name = product_Name;
        Product_Price = product_Price;
        Product_Detail = product_Detail;
        Product_Date = product_Date;
        Product_Time = product_Time;
        Product_Image_0=product_image_0;
        this.Product_ID=Product_ID;
        Product_Measure_In=product_measure_in;
        Product_Litre_Kilo=product_litre_kilo;
        Images_List=images_list;
        Product_No_Images=no_images;
        Verification =verification;
        this.user_type=user_type;
        this.Vendor_Contact_No=vendor_Contact_No;
        this.Cart=cart;
        this.Cart_product_quantity=cart_product_quantity;
        this.member_id=member_id;
        this.Product_Vendor_Type=product_Vendor_Type;


    }

    public DataModel(String product_Name, String product_Price, String product_Detail,String vendor_Contact_No,
                     String product_Date, String product_Time, String product_image_0,
                     String Product_ID, String product_measure_in,
                     String product_litre_kilo,String no_images , String product_Type,
                     ArrayList<String> images_list, String verification, String views) {
        Product_Name = product_Name;
        Product_Price = product_Price;
        Product_Detail = product_Detail;
        Product_Date = product_Date;
        Product_Time = product_Time;
        Product_Image_0=product_image_0;
        this.Product_ID=Product_ID;
        Product_Measure_In=product_measure_in;
        Product_Litre_Kilo=product_litre_kilo;
        Images_List=images_list;
        Product_No_Images=no_images;
        Verification =verification;
        Vendor_Contact_No=vendor_Contact_No;
        this.Product_Type=product_Type;
        this.Views=views;

    }
    public DataModel(String product_Name, String product_Price, String product_Detail,String vendor_Contact_No,
                     String product_Date, String product_Time, String product_image_0,
                     String Product_ID, String product_measure_in,
                     String product_litre_kilo,String no_images , String product_Type,
                     ArrayList<String> images_list, String verification) {
        Product_Name = product_Name;
        Product_Price = product_Price;
        Product_Detail = product_Detail;
        Product_Date = product_Date;
        Product_Time = product_Time;
        Product_Image_0=product_image_0;
        this.Product_ID=Product_ID;
        Product_Measure_In=product_measure_in;
        Product_Litre_Kilo=product_litre_kilo;
        Images_List=images_list;
        Product_No_Images=no_images;
        Verification =verification;
        Vendor_Contact_No=vendor_Contact_No;
        this.Product_Type=product_Type;

    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getVerification() {
        return Verification;
    }

    public void setVerification(String verification) {
        Verification = verification;
    }

    public String getProduct_No_Images() {
        return Product_No_Images;
    }

    public void setProduct_No_Images(String product_No_Images) {
        Product_No_Images = product_No_Images;
    }

    public ArrayList<String> getImages_List() {
        return Images_List;
    }

    public void setImages_List(ArrayList<String> images_List) {
        Images_List = images_List;
    }

    public String getProduct_Measure_In() {
        return Product_Measure_In;
    }

    public void setProduct_Measure_In(String product_Measure_In) {
        Product_Measure_In = product_Measure_In;
    }

    public String getProduct_Litre_Kilo() {
        return Product_Litre_Kilo;
    }

    public void setProduct_Litre_Kilo(String product_Litre_Kilo) {
        Product_Litre_Kilo = product_Litre_Kilo;
    }

    public String getProduct_ID() {
        return Product_ID;
    }

    public void setProduct_ID(String product_ID) {
        Product_ID = product_ID;
    }

    public String getProduct_Image_0() {
        return Product_Image_0;
    }

    public void setProduct_Image_0(String product_Image_0) {
        Product_Image_0 = product_Image_0;
    }

    public String getProduct_Date() {
        return Product_Date;
    }

    public void setProduct_Date(String product_Date) {
        Product_Date = product_Date;
    }

    public String getProduct_Time() {
        return Product_Time;
    }

    public void setProduct_Time(String product_Time) {
        Product_Time = product_Time;
    }

    public String getProduct_Name() {
        return Product_Name;
    }

    public void setProduct_Name(String product_Name) {
        Product_Name = product_Name;
    }

    public String getProduct_Price() {
        return Product_Price;
    }

    public void setProduct_Price(String product_Price) {
        Product_Price = product_Price;
    }

    public String getProduct_Detail() {
        return Product_Detail;
    }

    public void setProduct_Detail(String product_Detail) {
        Product_Detail = product_Detail;
    }

    public String getCart() {
        return Cart;
    }

    public void setCart(String cart) {
        Cart = cart;
    }

    public String getCart_product_quantity() {
        return Cart_product_quantity;
    }

    public void setCart_product_quantity(String cart_product_quantity) {
        Cart_product_quantity = cart_product_quantity;
    }

    String answer;
    String answer_city;
    String answer_name;
    String Domain_occupation;
    String answer_date;
    String image_1_ans;
    String image_2_ans;
    String image_3_ans;
    String answer_time;
    String recording_answer;

    String image_0;
    String image_1;
    String image_2;
    String image_3;
    ArrayList<String> Ques_Images_List;
    ArrayList<String> Ans_Images_List;




    String question;

    String recording;

    String question_city;
    String question_time;
    String question_date;
    String fasal;
    String question_phone;
    String answer_phone;
    String ID;
    String question_name;
    String member_id;
    String Fasal_Type_Shoba;


    int question_no;

    String extra_thing;

    public String getFasal_Type_Shoba() {
        return Fasal_Type_Shoba;
    }

    public void setFasal_Type_Shoba(String fasal_Type_Shoba) {
        Fasal_Type_Shoba = fasal_Type_Shoba;
    }

    public ArrayList<String> getAns_Images_List() {
        return Ans_Images_List;
    }

    public void setAns_Images_List(ArrayList<String> ans_Images_List) {
        Ans_Images_List = ans_Images_List;
    }

    public String getImage_0() {
        return image_0;
    }

    public void setImage_0(String image_0) {
        this.image_0 = image_0;
    }

    public ArrayList<String> getQues_Images_List() {
        return Ques_Images_List;
    }

    public void setQues_Images_List(ArrayList<String> ques_Images_List) {
        Ques_Images_List = ques_Images_List;
    }

    public String getExtra_thing() {
        return extra_thing;
    }

    public void setExtra_thing(String extra_thing) {
        this.extra_thing = extra_thing;
    }

    public String getDomain_occupation() {
        return Domain_occupation;
    }

    public void setDomain_occupation(String domain_occupation) {
        Domain_occupation = domain_occupation;
    }

    public String getRecording_answer() {
        return recording_answer;
    }

    public void setRecording_answer(String recording_answer) {
        this.recording_answer = recording_answer;
    }

    public String getAnswer_date() {
        return answer_date;
    }

    public void setAnswer_date(String answer_date) {
        this.answer_date = answer_date;
    }

    public String getImage_1_ans() {
        return image_1_ans;
    }

    public void setImage_1_ans(String image_1_ans) {
        this.image_1_ans = image_1_ans;
    }

    public String getImage_2_ans() {
        return image_2_ans;
    }

    public void setImage_2_ans(String image_2_ans) {
        this.image_2_ans = image_2_ans;
    }

    public String getImage_3_ans() {
        return image_3_ans;
    }

    public void setImage_3_ans(String image_3_ans) {
        this.image_3_ans = image_3_ans;
    }

    public int getQuestion_no() {
        return question_no;
    }

    public void setQuestion_no(int question_no) {
        this.question_no = question_no;
    }

    public String getQuestion_date() {
        return question_date;
    }

    public void setQuestion_date(String question_date) {
        this.question_date = question_date;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getRecording() {
        return recording;
    }

    public void setRecording(String recording) {
        this.recording = recording;
    }

    public DataModel(String fasal_type, String shoba_type){
        this.fasal=fasal_type;
        this.Fasal_Type_Shoba=shoba_type;
    }


    /////awami raye member NEW
    public DataModel(String question, String question_city, String question_time, String fasal,
                     String ID, String question_name, String recording,
                     String image_1, ArrayList<String> Ques_Images_List,ArrayList<String> Ans_Images_List, String member_id,
                     String question_date,
                     String answer, String answer_city, String answer_name,
                     String answer_date, String answer_time
            , String occupation,String recording_answer, String extra_thing, String user_type) {
        this.question = question;
        this.question_city = question_city;
        this.question_time = question_time;
        this.fasal = fasal;
        this.ID = ID;
        this.recording = recording;
        this.question_name = question_name;
        this.image_1 = image_1;
        this.image_2 = image_2;
        this.image_3 = image_3;
        this.member_id=member_id;
        this.question_date=question_date;

        this.recording_answer=recording_answer;
        this.answer_name=answer_name;
        this.answer=answer;
        this.answer_city=answer_city;
        this.answer_name=answer_name;
        this.answer_date=answer_date;
        this.answer_time=answer_time;
        this.image_1_ans=image_1_ans;
        this.image_2_ans=image_2_ans;
        this.image_3_ans=image_3_ans;
        this.Domain_occupation=occupation;
        this.extra_thing=extra_thing;
        this.Ques_Images_List=Ques_Images_List;
        this.Ans_Images_List=Ans_Images_List;
        this.user_type=user_type;

    }

    /////awami raye member OLD
    public DataModel(String question, String question_city, String question_time, String fasal,
                     String ID, String question_name, String recording,
                     String image_1, String image_2, String image_3, String member_id,
                     String question_date,
                     String answer, String answer_city, String answer_name,
                     String answer_date, String answer_time, String image_1_ans,
                     String image_2_ans,String image_3_ans, String occupation,String recording_answer, String extra_thing) {
        this.question = question;
        this.question_city = question_city;
        this.question_time = question_time;
        this.fasal = fasal;
        this.ID = ID;
        this.recording = recording;
        this.question_name = question_name;
        this.image_1 = image_1;
        this.image_2 = image_2;
        this.image_3 = image_3;
        this.member_id=member_id;
        this.question_date=question_date;

        this.recording_answer=recording_answer;
        this.answer_name=answer_name;
        this.answer=answer;
        this.answer_city=answer_city;
        this.answer_name=answer_name;
        this.answer_date=answer_date;
        this.answer_time=answer_time;
        this.image_1_ans=image_1_ans;
        this.image_2_ans=image_2_ans;
        this.image_3_ans=image_3_ans;
        this.Domain_occupation=occupation;
        this.extra_thing=extra_thing;


    }
    //////////////////Member VIEW ONE QUESTION AND ANSWER NEW FOR MULTIPLE IAMGES OF QUESTIONS AND ANSWER
    public DataModel(String question, String question_city, String question_time, String fasal,
                     String ID, String question_name, String recording,
                     String image_0,ArrayList<String> Ques_Images_List,ArrayList<String> Ans_Images_List, String member_id,
                     String question_date, int question_no,
                     String answer, String answer_city, String answer_name,
                     String answer_date, String answer_time
                     , String occupation,String recording_answer) {
        this.question = question;
        this.question_city = question_city;
        this.question_time = question_time;
        this.fasal = fasal;
        this.ID = ID;
        this.recording = recording;
        this.question_name = question_name;
        this.image_0 = image_0;
        this.member_id=member_id;
        this.question_date=question_date;
        this.question_no=question_no;

        this.recording_answer=recording_answer;
        this.answer_name=answer_name;
        this.answer=answer;
        this.answer_city=answer_city;
        this.answer_name=answer_name;
        this.answer_date=answer_date;
        this.answer_time=answer_time;
        this.image_1_ans=image_1_ans;
        this.image_2_ans=image_2_ans;
        this.image_3_ans=image_3_ans;
        this.Domain_occupation=occupation;
        this.Ques_Images_List=Ques_Images_List;
        this.Ans_Images_List=Ans_Images_List;


    }
    //////////////////Member VIEW ONE QUESTION AND ANSWER OLD
    public DataModel(String question, String question_city, String question_time, String fasal,
                     String ID, String question_name, String recording,
                     String image_0,ArrayList<String> Ques_Images_List, String member_id,
                     String question_date, int question_no,
                     String answer, String answer_city, String answer_name,
                     String answer_date, String answer_time, String image_1_ans,
                     String image_2_ans,String image_3_ans, String occupation,String recording_answer) {
        this.question = question;
        this.question_city = question_city;
        this.question_time = question_time;
        this.fasal = fasal;
        this.ID = ID;
        this.recording = recording;
        this.question_name = question_name;
        this.image_0 = image_0;
        this.member_id=member_id;
        this.question_date=question_date;
        this.question_no=question_no;

        this.recording_answer=recording_answer;
        this.answer_name=answer_name;
        this.answer=answer;
        this.answer_city=answer_city;
        this.answer_name=answer_name;
        this.answer_date=answer_date;
        this.answer_time=answer_time;
        this.image_1_ans=image_1_ans;
        this.image_2_ans=image_2_ans;
        this.image_3_ans=image_3_ans;
        this.Domain_occupation=occupation;
        this.Ques_Images_List=Ques_Images_List;


    }

    ////to see one question answer for member
    public DataModel(String question, String question_city, String question_time, String fasal,
                     String ID, String question_name, String recording,
                     String image_1, String image_2, String image_3, String member_id,
                     String question_date, int question_no,
                     String answer, String answer_city, String answer_name,
                     String answer_date, String answer_time, String image_1_ans,
                     String image_2_ans,String image_3_ans, String occupation,String recording_answer) {
        this.question = question;
        this.question_city = question_city;
        this.question_time = question_time;
        this.fasal = fasal;
        this.ID = ID;
        this.recording = recording;
        this.question_name = question_name;
        this.image_1 = image_1;
        this.image_2 = image_2;
        this.image_3 = image_3;
        this.member_id=member_id;
        this.question_date=question_date;
        this.question_no=question_no;

        this.recording_answer=recording_answer;
        this.answer_name=answer_name;
        this.answer=answer;
        this.answer_city=answer_city;
        this.answer_name=answer_name;
        this.answer_date=answer_date;
        this.answer_time=answer_time;
        this.image_1_ans=image_1_ans;
        this.image_2_ans=image_2_ans;
        this.image_3_ans=image_3_ans;
        this.Domain_occupation=occupation;


    }


    ///main constructor
    public DataModel(String question, String question_city, String question_time, String fasal,
                     String ID, String question_name, String recording,
                     String image_0, ArrayList<String> Ques_Images_List, String member_id, String question_date) {
        this.question = question;
        this.question_city = question_city;
        this.question_time = question_time;
        this.fasal = fasal;
        this.ID = ID;
        this.recording = recording;
        this.question_name = question_name;
        this.image_0 = image_0;
        this.member_id=member_id;
        this.question_date=question_date;
        this.Ques_Images_List=Ques_Images_List;

    }

    public DataModel(String question, String question_city, String question_time, String fasal,
                     String ID, String question_name, String recording,
                     String image_1, String image_2, String image_3, String member_id, String question_date, int question_no) {
        this.question = question;
        this.question_city = question_city;
        this.question_time = question_time;
        this.fasal = fasal;
        this.ID = ID;
        this.recording = recording;
        this.question_name = question_name;
        this.image_1 = image_1;
        this.image_2 = image_2;
        this.image_3 = image_3;
        this.member_id=member_id;
        this.question_date=question_date;
        this.question_no=question_no;

    }


    public DataModel(String question, String answer, String question_city, String answer_city, String question_time, String answer_time,
                     String occupation, String fasal, String question_phone, String answer_phone, String ID, String question_name, String answer_name,
                     String name, String type, String version_number, String feature) {
        this.question = question;
        this.answer = answer;
        this.question_city = question_city;
        this.answer_city = answer_city;
        this.question_time = question_time;
        this.answer_time = answer_time;
        this.Domain_occupation = occupation;
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
        return Domain_occupation;
    }

    public void setOccupation(String occupation) {
        this.Domain_occupation = occupation;
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

    public DataModel(String name, String type, String version_number, String feature ) {
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



    public String getImage_1() {
        return image_1;
    }

    public void setImage_1(String image_1) {
        this.image_1 = image_1;
    }

    public String getImage_2() {
        return image_2;
    }

    public void setImage_2(String image_2) {
        this.image_2 = image_2;
    }

    public String getImage_3() {
        return image_3;
    }

    public void setImage_3(String image_3) {
        this.image_3 = image_3;
    }

    public String getVendor_Contact_No() {
        return Vendor_Contact_No;
    }

    public void setVendor_Contact_No(String vendor_Contact_No) {
        Vendor_Contact_No = vendor_Contact_No;
    }

    public String getProduct_Type() {
        return Product_Type;
    }

    public void setProduct_Type(String product_Type) {
        Product_Type = product_Type;
    }
}

