package agri_asan.com.agriasan06_12_19;

public class Upload {
    //private String mName;
    private String mImageUrl;

    public Upload() {
        //empty constructor needed
    }

    public Upload(String imageUrl) {
        mImageUrl = imageUrl;
    }
//
//    public String getName() {
//        return mName;
//    }

//    public void setName(String name) {
//        mName = name;
//    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }
}
