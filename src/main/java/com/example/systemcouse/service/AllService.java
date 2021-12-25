package com.example.systemcouse.service;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.jms.*;
import java.util.List;
import com.example.systemcouse.model.*;
import com.example.systemcouse.repository.*;


@Stateless
public class AllService {

    @EJB
    private RankRepository rankRepository;

    @EJB
    private TaskRepository taskRepository;

    @EJB
    private UserRepository userRepository;

    @EJB
    private TutorRepository tutorRepository;

    @EJB
    private QualityRepository qualityRepository;

    @Resource(name = "messageQueue")
    private Queue messageQueue;

    @Resource(name = "commonQueue")
    private Queue commonQueue;

    @Resource
    private ConnectionFactory connectionFactory;

    //Task//

    public List<Task> selectTasks() {
        return taskRepository.selectTasks();
    }

    public Task selectTask(int taskId){
        return taskRepository.selectTask(taskId);
    }

    public Task insertTask(Task task){
        return taskRepository.insertTask(task);
    }

    public boolean deleteTask(int id) {
        return taskRepository.deleteMap(id);
    }

    public boolean updateTask(Task task) {
        return taskRepository.updateTask(task);
    }

    //Rank//
    public List<Rank> selectRanks() {return rankRepository.selectRanks();}

    public Rank selectRank(int rankId) {return rankRepository.selectRank(rankId);}

    public Rank insertRank(Rank rank) {return rankRepository.insertRank(rank);}

    public boolean deleteRank(int id) {
        return rankRepository.deleteRank(id);
    }

    public boolean updateRank(Rank rank) {
        return rankRepository.updateRank(rank);
    }

    //TUTOR //
    public Tutor insertTutor(Tutor tutor) {
        return tutorRepository.insertTutor(tutor);
    }

    public List<Tutor> selectTutors() {
        return tutorRepository.selectTutors();
    }

    public Tutor selectTutor(int tutorId) {
        return tutorRepository.selectTutor(tutorId);
    }

    public boolean deleteTutor(int id) {
        return tutorRepository.deleteTutor(id);
    }

    public boolean updateTutor(Tutor tutor) {
        return tutorRepository.updateTutor(tutor);
    }

    //QUALITY//
    public Quality insertQualities(Quality quality) {
        return qualityRepository.insertQualities(quality);
    }

    public List<Quality> selectQualities() {
        return qualityRepository.selectQualities();
    }

    public Quality selectQuality(int qualityId) {
        return qualityRepository.selectQuality(qualityId);
    }

    public boolean deleteQuality(int id) {
        return qualityRepository.deleteQuality(id);
    }

    public boolean updateQuality(Quality quality) {
        return qualityRepository.updateQuality(quality);
    }

    //USER//
    public User_ insertUser(User_ user){return userRepository.insertUser(user);}

    public List<User_> selectUsers() {
        return userRepository.selectUsers();
    }

    public User_ selectUser(int userId)  {
        return userRepository.selectUser(userId);
    }

    public boolean deleteUser(int id) {
        return userRepository.deleteUser(id);
    }

    public boolean updateUser(User_ user){
        return userRepository.updateUser(user);
    }


    //JMS//
    public String sendJmsMessage(String message) {
        try (final Connection connection = connectionFactory.createConnection();
             final Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
             final MessageProducer producer = session.createProducer(messageQueue);
             final MessageProducer producer2 = session.createProducer(commonQueue)) {
            connection.start();
            final Message jmsMessage = session.createTextMessage(message);
            producer.send(jmsMessage);
            producer2.send(jmsMessage);
            return "successfylly sended";
        } catch (final Exception e) {
            throw new RuntimeException("Caught exception from JMS when sending a message", e);
        }
    }

    public String getMessage() {
        try (final Connection connection = connectionFactory.createConnection();
             final Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
             final MessageConsumer messageConsumer = session.createConsumer(messageQueue)) {

            connection.start();

            final Message jmsMessage = messageConsumer.receive(1000);

            if (jmsMessage == null) {
                return "jmsMessage is null";
            }

            TextMessage textMessage = (TextMessage) jmsMessage;
            if (textMessage == null) {
                return "Empty textMessage";
            }
            return textMessage.getText();
        } catch (final Exception e) {
            throw new RuntimeException("Caught exception from JMS when receiving a message", e);
        }
    }

    public Object getCommon() {
        try (final Connection connection = connectionFactory.createConnection();
             final Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
             final MessageConsumer messageConsumer = session.createConsumer(commonQueue)) {

            connection.start();

            final Message jmsMessage = messageConsumer.receive(1000);

            if (jmsMessage == null) {
                return "jmsMessage is null";
            }

            TextMessage textMessage = (TextMessage) jmsMessage;
            if (textMessage == null) {
                return "Empty textMessage";
            }
            return textMessage.getText();
        } catch (final Exception e) {
            throw new RuntimeException("Caught exception from JMS when receiving a message", e);
        }
    }
}
