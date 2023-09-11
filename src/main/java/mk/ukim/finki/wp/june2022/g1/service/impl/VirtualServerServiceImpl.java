package mk.ukim.finki.wp.june2022.g1.service.impl;

import mk.ukim.finki.wp.june2022.g1.model.OSType;
import mk.ukim.finki.wp.june2022.g1.model.User;
import mk.ukim.finki.wp.june2022.g1.model.VirtualServer;
import mk.ukim.finki.wp.june2022.g1.model.exceptions.InvalidUserIdException;
import mk.ukim.finki.wp.june2022.g1.model.exceptions.InvalidVirtualMachineIdException;
import mk.ukim.finki.wp.june2022.g1.repository.UserRepository;
import mk.ukim.finki.wp.june2022.g1.repository.VirtualServerRepository;
import mk.ukim.finki.wp.june2022.g1.service.VirtualServerService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VirtualServerServiceImpl implements VirtualServerService {

    private final UserRepository userRepository;
    private final VirtualServerRepository virtualServerRepository;

    public VirtualServerServiceImpl(UserRepository userRepository, VirtualServerRepository virtualServerRepository) {
        this.userRepository = userRepository;
        this.virtualServerRepository = virtualServerRepository;
    }

    @Override
    public List<VirtualServer> listAll() {
        return this.virtualServerRepository.findAll();
    }

    @Override
    public VirtualServer findById(Long id) {
        return this.virtualServerRepository.findById(id).orElseThrow(InvalidVirtualMachineIdException::new);
    }

    @Override
    public VirtualServer create(String name, String ipAddress, OSType osType, List<Long> owners, LocalDate launchDate) {
        List<User> users= userRepository.findAllById(owners);
        VirtualServer virtualServer= new VirtualServer(name, ipAddress, osType, users, launchDate);

        return this.virtualServerRepository.save(virtualServer);
    }

    @Override
    public VirtualServer update(Long id, String name, String ipAddress, OSType osType, List<Long> owners) {
        VirtualServer virtualServer=this.findById(id);
        List<User> users= userRepository.findAllById(owners);
        virtualServer.setId(id);
        virtualServer.setInstanceName(name);
        virtualServer.setIpAddress(ipAddress);
        virtualServer.setOSType(osType);
        virtualServer.setOwners(users);

        return this.virtualServerRepository.save(virtualServer);
    }

    @Override
    public VirtualServer delete(Long id) {
        VirtualServer virtualServer=this.findById(id);
        this.virtualServerRepository.delete(virtualServer);
        return virtualServer;
    }

    @Override
    public VirtualServer markTerminated(Long id) {
        VirtualServer virtualServer=this.findById(id);
        virtualServer.setTerminated(true);
        return this.virtualServerRepository.save(virtualServer);
    }

    @Override
    public List<VirtualServer> filter(Long ownerId, Integer activeMoreThanDays) {
        if(ownerId == null && activeMoreThanDays==null){
            return this.virtualServerRepository.findAll();
        }else if(ownerId==null){
            return this.virtualServerRepository.findAll().stream().filter(emp -> LocalDate.now().getYear() - emp.getLaunchDate().getYear() > activeMoreThanDays).collect(Collectors.toList());
        }else if(activeMoreThanDays==null){
            User user= this.userRepository.findById(ownerId).orElseThrow(InvalidUserIdException::new);
            return this.virtualServerRepository.findByOwners(user);
        }else{
            User user= this.userRepository.findById(ownerId).orElseThrow(InvalidUserIdException::new);
            return this.virtualServerRepository.findAll().stream().filter(emp -> LocalDate.now().getYear() - emp.getLaunchDate().getYear() > activeMoreThanDays).collect(Collectors.toList());
        }
    }
}
