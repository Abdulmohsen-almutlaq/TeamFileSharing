package service;

import java.util.List;

import model.Team;
import repository.TeamRepository;

public class TeamService {

    private TeamRepository repo = new TeamRepository();

    // CREATE
    public void createTeam(String name) {
        Team team = new Team(0, name); 
        repo.create(team);
        System.out.println("Team created: " + name);
    }

    // READ by ID
    public Team getTeam(int id) {
        return repo.findById(id);
    }

    // READ all
    public List<Team> getAllTeams() {
        return repo.findAll();
    }

    // UPDATE
    public void updateTeam(int id, String newName) {
        Team team = new Team(id, newName);
        repo.update(team);
        System.out.println("Team updated (ID " + id + ")");
    }

    // DELETE
    public void deleteTeam(int id) {
        repo.delete(id);
        System.out.println("Team deleted (ID " + id + ")");
    }
}
