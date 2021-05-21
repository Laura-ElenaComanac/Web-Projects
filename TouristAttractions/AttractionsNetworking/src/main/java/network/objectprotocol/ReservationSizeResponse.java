package network.objectprotocol;

public class ReservationSizeResponse implements Response{
    int size;

    public ReservationSizeResponse(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
