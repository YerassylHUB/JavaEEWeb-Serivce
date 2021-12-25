package com.example.systemcouse.control;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.example.systemcouse.model.*;
import com.example.systemcouse.repository.TaskRepository;
import com.example.systemcouse.service.*;

import java.sql.Date;
import java.util.List;

@Path("/courseSystem")
public class RestResult {

    @EJB
    private AllService allService;

    @EJB
    private TaskRepository taskRepository;

    //EXERCISES RESTS//
    @GET
    @Path("/selectExercises")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response selectTasks() {
        if(allService.selectTasks() != null) {
            return Response.ok(allService.selectTasks()).build();
        }
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @GET
    @Path("/selectExercise/{id}")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response selectTask(@PathParam("id") int taskId) {
        if(allService.selectTask(taskId) != null) {
            return Response.ok(allService.selectTask(taskId)).build();
        }
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertExercise")
    @RolesAllowed("ADMIN")
    public Response insertTask(Task task){
        if(allService.insertTask(task).getTaskId() != null) // проверка
            return Response.ok(task).build();
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }

    @PUT
    @Path("/updateExercises/{exerciseId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @RolesAllowed({"ADMIN","INSTRUCTOR"})
    public Response updateTask(
            @PathParam("exerciseId") long id,
            @FormParam("taskName") String taskName) {
        if(allService.updateTask(new Task(id, taskName)))
            return Response.ok().entity("updated").build();
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }

    @DELETE
    @Path("/deleteExercise/{exerciseId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public Response deleteTask(
            @PathParam("exerciseId") int id) {
        if(allService.deleteTask(id))
            return Response.ok().entity("Deleted").build();
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }


    // RATINGS RESTS//
    @GET
    @Path("/selectRatings")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response selectRanks() {
        if(allService.selectUsers() != null)
            return Response.ok(allService.selectRanks()).build();
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }

    //Score RESTS//
    @GET
    @Path("/selectRating/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response selectRank(@PathParam("id") int rankId) {
        if(allService.selectRank(rankId) != null) {
            return Response.ok(allService.selectRank(rankId)).build();
        }
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }

    @PUT
    @Path("/updateRating/{Id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @RolesAllowed({"ADMIN"})
    public Response updateRank(
            @PathParam("Id") long id,
            @FormParam("rankName") String scoreName) {
        if(allService.updateRank(new Rank(id, scoreName)))
            return Response.ok().entity("updated").build();
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertRating")
    @RolesAllowed("ADMIN")
    public Response insertRank(Rank rank) {
        if(allService.insertRank(rank).getRankId() != null)
            return Response.ok(rank).build();
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }

    @DELETE
    @Path("/deleteRating/{Id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN","INSTRUCTOR"})
    public Response deleteRank(
            @PathParam("Id") int id) {
        if(allService.deleteRank(id)) {
            return Response.ok().entity("Deleted").build();
        }
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }

    //User RESTS//
    @GET
    @Path("/selectUsers")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response selectUsers() {
        if(allService.selectUsers() != null)
            return Response.ok(allService.selectUsers()).build();
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }

    @GET
    @Path("/selectUser/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response selectUser(@PathParam("id") int userId) {
        if(allService.selectUser(userId) != null)
            return Response.ok(allService.selectUser(userId)).build();
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }

    @PUT
    @Path("/updateUser/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @RolesAllowed("USER")
    public Response updateUser(
            @PathParam("userId") long id,
            @FormParam("userName") String userName,
            @FormParam("lastSession") Date lastSession,
            @FormParam("rankId") long rankId
    ) {
        List<Rank> ranks = allService.selectRanks();
        Rank rank = new Rank();
        for (Rank i:ranks) {
            if(rankId == i.getRankId())
                rank = i;
        }
        if(allService.updateUser
                (new User_(id, userName,lastSession.toLocalDate(), rank)))
            return Response.ok().entity("updated").build();
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertUser")
    @RolesAllowed("ADMIN")
    public Response insertUser(User_ user) {
        if(allService.insertUser(user).getUserId() != null)
            return Response.ok(user).build();
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }

    @DELETE
    @Path("/deleteUser/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public Response deleteUser(
            @PathParam("userId") int id) {
        if(allService.deleteUser(id))
            return Response.ok().entity("Deleted").build();
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }

    //ADVISOR RESTS//
    @GET
    @Path("/selectAdvisers")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response selectTutors() {
        if(allService.selectTutors() != null) {
            return Response.ok(allService.selectTutors()).build();
        }
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @GET
    @Path("/selectAdviser/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response selectTutor(@PathParam("id") int tutorId) {
        if(allService.selectTutor(tutorId) != null) {
            return Response.ok(allService.selectTutor(tutorId)).build();
        }
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertAdviser")
    @RolesAllowed("ADMIN")
    public Response insertTutor(Tutor tutor){
        if(allService.insertTutor(tutor).getTutorId() != null)
            return Response.ok(tutor).build();
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }

    @PUT
    @Path("/updateAdviser/{Id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @RolesAllowed("ADMIN")
    public Response updateAgent(
            @PathParam("Id") long id,
            @FormParam("tutorName") String tutorName,
            @FormParam("specialization") String specialization,
            @FormParam("description") String description) {
        if(allService.updateTutor(new Tutor(id, tutorName, specialization, description)))
            return Response.ok().entity("updated").build();
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }

    @DELETE
    @Path("/deleteAdviser/{Id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public Response deleteAgent(
            @PathParam("Id") int id) {
        if(allService.deleteTutor(id))
            return Response.ok().entity("Deleted").build();
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }

    //Quality RESTS//
    @GET
    @Path("/selectQualities")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response selectQualities() {
        if(allService.selectQualities() != null) {
            return Response.ok(allService.selectQualities()).build();
        }
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @GET
    @Path("/selectQuality/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response selectQuality(@PathParam("id") int qualityId) {
        if(allService.selectQuality(qualityId) != null) {
            return Response.ok(allService.selectQuality(qualityId)).build();
        }
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertQualities")
    @RolesAllowed("ADMIN")
    public Response insertQualities(Quality quality){
        if(allService.insertQualities(quality).getQualityId() != null)
            return Response.ok(quality).build();
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }

    @PUT
    @Path("/updateTutor/{qualityId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @RolesAllowed("ADMIN")
    public Response updateQuality(
            @PathParam("qualityId") long id,
            @FormParam("quality_name") String qualityName,
            @FormParam("tutor_id") long tutorId,
            @FormParam("tutor_name") String tutorName,
            @FormParam("specialization") String specialization,
            @FormParam("description") String description) {
        Tutor tutor = new Tutor(tutorId, tutorName, specialization, description);
        if(allService.updateQuality(new Quality(id, qualityName, tutor)))
            return Response.ok().entity("updated").build();
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }

    @DELETE
    @Path("/deleteQuality/{qualityId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public Response deleteQuality(
            @PathParam("qualityId") int id) {
        if(allService.deleteQuality(id))
            return Response.ok().entity("Deleted").build();
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error 500").build();
    }

    //JMS//
    @POST
    @Path("/jms")
    @PermitAll
    public String sendMessage(String message) {
        return allService.sendJmsMessage(message);
    }

    @GET
    @Path("/jms")
    @RolesAllowed("ADMIN")
    public String getMessage() {
        return allService.getMessage();
    }

    @GET
    @Path("/common")
    @RolesAllowed("ADMIN")
    public Response getCommon() {
        return Response.ok(allService.getCommon()).build();
    }

}
