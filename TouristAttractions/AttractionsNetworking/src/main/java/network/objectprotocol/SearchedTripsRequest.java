package network.objectprotocol;

import network.dto.SearchedTripDTO;

public class SearchedTripsRequest implements Request{
    private SearchedTripDTO searchedTripDTO;

    public SearchedTripsRequest(SearchedTripDTO searchedTripDTO) {
        this.searchedTripDTO = searchedTripDTO;
    }

    public SearchedTripDTO getSearchedTripDTO() {
        return searchedTripDTO;
    }
}
