package dto;

public class RemindDTO {
    private String title;
    private String doc_DTO;
    private String img_DTO;
    private String data_DTO;

    public String getImg_DTO() {
        return img_DTO;
    }

    public void setImg_DTO(String img_DTO) {
        this.img_DTO = img_DTO;
    }



    public String getDoc_DTO() {
        return doc_DTO;
    }

    public void setDoc_DTO(String doc_DTO) {
        this.doc_DTO = doc_DTO;
    }



    public RemindDTO(String title,String doc_DTO,String img_DTO,String data_DTO) {
        this.title = title;
        this.doc_DTO = doc_DTO;
        this.img_DTO = img_DTO;
        this.data_DTO = data_DTO;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getData_DTO() {
        return data_DTO;
    }

    public void setData_DTO(String data_DTO) {
        this.data_DTO = data_DTO;
    }
}
