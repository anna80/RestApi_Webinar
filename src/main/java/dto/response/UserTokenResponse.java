package dto.response;

import lombok.Data;

@Data
public class UserTokenResponse {

    public String token;
    public String expires;
    public String status;
    public String result;
}
