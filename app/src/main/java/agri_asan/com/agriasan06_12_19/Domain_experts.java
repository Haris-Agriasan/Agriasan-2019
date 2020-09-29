package agri_asan.com.agriasan06_12_19;

public class Domain_experts {


    private String Domain_Verification;

    private String Imei;

    private String Vendor_Verification;
    private String Name;
    private String Phone;
    private String ID_Card;
    private String City;
    private String ID;
    private String Occupation;
    private String Vendor;

    public String getDomain_Verification() {
        return Domain_Verification;
    }

    public void setDomain_Verification(String domain_Verification) {
        Domain_Verification = domain_Verification;
    }

    public String getImei() {
        return Imei;
    }

    public void setImei(String imei) {
        Imei = imei;
    }

    public String getVendor_Verification() {
        return Vendor_Verification;
    }

    public void setVendor_Verification(String vendor_Verification) {
        Vendor_Verification = vendor_Verification;
    }

    public String getVendor() {
        return Vendor;
    }

    public void setVendor(String vendor) {
        Vendor = vendor;
    }

    public String getOccupation() {
        return Occupation;
    }

    public void setOccupation(String occupation) {
        Occupation = occupation;
    }

    public Domain_experts(){
        //this constructor is required
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getID_Card() {
        return ID_Card;
    }

    public void setID_Card(String ID_Card) {
        this.ID_Card = ID_Card;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Domain_experts(String name, String phone, String ID_Card, String city, String ID, String occupation) {
        Name = name;
        Phone = phone;
        this.ID_Card = ID_Card;
        City = city;
        this.ID = ID;
        this.Occupation = occupation;

    }
}
