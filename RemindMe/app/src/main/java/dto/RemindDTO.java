package dto;

public class RemindDTO {
    private String title;
    private String doc_DTO;
    private String img_DTO;
    private String data_DTO;
    private String url_DTO;
    private Integer node;

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



    public RemindDTO(String title,String doc_DTO,String img_DTO,String data_DTO,String url_DTO) {
        this.title = title;
        this.doc_DTO = doc_DTO;
        this.img_DTO = img_DTO;
        this.data_DTO = data_DTO;
        this.url_DTO = url_DTO;
        this.node= 0;
    }
    public RemindDTO(String title,String doc_DTO,String img_DTO,String data_DTO,String url_DTO,Integer node) {
        this.title = title;
        this.doc_DTO = doc_DTO;
        this.img_DTO = img_DTO;
        this.data_DTO = data_DTO;
        this.url_DTO = url_DTO;
        this.node= node;
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

    public String getUrl_DTO() {
        return url_DTO;
    }

    public void setUrl_DTO(String url_DTO) {
        this.url_DTO = url_DTO;
    }

    public Integer getNode() {
        return node;
    }

    public void setNode(Integer node) {
        this.node = node;
    }
}
