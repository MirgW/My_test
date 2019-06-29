package dto;

import java.util.ArrayList;
import java.util.List;

public class ServiceDTO {
    private List<RemindDTO> data_old = new ArrayList<>();
    private List<RemindDTO> data_new = new ArrayList<>();

    public ServiceDTO() {
        this.data_new = new ArrayList<>();
        this.data_old = new ArrayList<>();
    }

    public List<RemindDTO> getNew() {
        final ArrayList<RemindDTO> added = new ArrayList<>();
        for (RemindDTO remindDTO : data_new) {
            boolean contained = false;
            for (RemindDTO remindDTO_old : data_old) {
                if (remindDTO.getNode().equals(remindDTO_old.getNode())) {
                    contained = true;
                }
            }
            if (!contained) {
                added.add(remindDTO);
            }
        }
        return added;
    }

    public List<RemindDTO> getData_old() {
        return data_old;
    }

    public void setData_old(List<RemindDTO> data_old) {
        this.data_old = data_old;
    }

    public List<RemindDTO> getData_new() {
        return data_new;
    }

    public void setData_new(List<RemindDTO> data_new) {
        this.data_new = data_new;
    }

    public void creatMockData() {
        data_old.clear();
        data_old.add(new RemindDTO("", "", "", "", "",17537));
        data_old.add(new RemindDTO("", "", "", "", "",17551));
        data_old.add(new RemindDTO("", "", "", "", "",17550));
        data_old.add(new RemindDTO("", "", "", "", "",17549));
        data_old.add(new RemindDTO("", "", "", "", "",17548));
        data_old.add(new RemindDTO("", "", "", "", "",17547));
        data_old.add(new RemindDTO("", "", "", "", "",17545));
        data_old.add(new RemindDTO("", "", "", "", "",17544));
        data_old.add(new RemindDTO("", "", "", "", "",17543));
        data_old.add(new RemindDTO("", "", "", "", "",17542));
        data_old.add(new RemindDTO("", "", "", "", "",17541));
        data_old.add(new RemindDTO("", "", "", "", "",17539));
        data_old.add(new RemindDTO("", "", "", "", "",17535));
        data_old.add(new RemindDTO("", "", "", "", "",17538));
    }

}
