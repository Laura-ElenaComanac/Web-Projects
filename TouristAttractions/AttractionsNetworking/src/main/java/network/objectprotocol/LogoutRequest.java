package network.objectprotocol;

import model.AgencyUser;

public class LogoutRequest implements Request{
    AgencyUser agencyUser;

    public LogoutRequest(AgencyUser agencyUser) {
        this.agencyUser = agencyUser;
    }

    public AgencyUser getAgencyUser() {
        return agencyUser;
    }
}
