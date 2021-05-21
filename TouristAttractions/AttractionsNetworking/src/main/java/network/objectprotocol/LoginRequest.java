package network.objectprotocol;

import model.AgencyUser;

public class LoginRequest implements Request{
    AgencyUser agencyUser;

    public LoginRequest(AgencyUser agencyUser) {
        this.agencyUser = agencyUser;
    }

    public AgencyUser getAgencyUser() {
        return agencyUser;
    }
}
