package cc.ddev.feather.database.models;

import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.api.markers.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "phonemessages")
public class PhoneMessagesModel extends StormModel {

    @Column(name = "sender")
    private Integer senderPhoneNumber;

    @Column(name = "receiver")
    private Integer receiverPhoneNumber;

    @Column(name = "message")
    private String message;

    @Column(name = "message_read")
    private Boolean messageRead;
}