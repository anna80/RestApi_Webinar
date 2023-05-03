package dto.response;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserNotFoundResponse {
    @SerializedName("code")
    public String code;
    @SerializedName("message")
    public String message;


}
