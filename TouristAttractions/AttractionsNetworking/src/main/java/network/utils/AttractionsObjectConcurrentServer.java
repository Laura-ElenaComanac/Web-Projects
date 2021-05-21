package network.utils;


import network.objectprotocol.AttractionsClientObjectWorker;
import service.IService;

import java.net.Socket;


public class AttractionsObjectConcurrentServer extends AbsConcurrentServer {
    private IService service;
    public AttractionsObjectConcurrentServer(int port, IService service) {
        super(port);
        this.service = service;
        System.out.println("TouristAttractions- AttractionsObjectConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        AttractionsClientObjectWorker worker=new AttractionsClientObjectWorker(service, client);
        Thread tw=new Thread(worker);
        return tw;
    }


}