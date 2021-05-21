package network.objectprotocol;

import model.AgencyUser;
import network.dto.AgencyUserDTO;

public class FindAgencyUserRequest implements Request {
    private AgencyUserDTO user;

    public FindAgencyUserRequest(AgencyUserDTO user) {
        this.user = user;
    }

    public AgencyUserDTO getUser() {
        return user;
    }
}
