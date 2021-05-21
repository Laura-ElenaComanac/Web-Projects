package network.objectprotocol;

import model.AgencyUser;

public class FindAgencyUserResponse implements Response{
    private AgencyUser agencyUser;

    public FindAgencyUserResponse(AgencyUser agencyUser){
        this.agencyUser = agencyUser;
    }

    public AgencyUser getAgencyUser() {
        return agencyUser;
    }
}
