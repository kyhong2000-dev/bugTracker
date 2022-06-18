package com.bugTracker;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bugTracker.entity.Project;
import com.bugTracker.entity.Ticket;
import com.bugTracker.entity.User;
import com.bugTracker.service.ProjectService;
import com.bugTracker.service.TicketService;
import com.bugTracker.service.UserService;

@Controller
public class AppController {
	/**
	 * POST:   Creates a new resource
	 * GET:    Reads a resource
  	 * PUT:    Updates an existing resource
  	 * DELETE: Deletes a resource
	 **/
	
	@Autowired
	private UserService userService;
	
	@Autowired 
	private ProjectService projectService;
	
	@Autowired 
	private TicketService ticketService;
	
	
	@GetMapping("/")
	public String viewHomePage() {
		return "index";
	}
	
	@GetMapping("/signup")
	public String displaySignUpPageForm(Model model) {
		model.addAttribute("user", new User());
		return "signup";
	}
	
	@GetMapping("/login")
	public String displayLoginPageForm() {
		return "login";
	}
	
	@PostMapping("/registerAccount")
	public String processRegisterNewAccount(Model model, @ModelAttribute User user){
		if(!User.isValidUserDetails(user)) {
			return "redirect:/signup?error";
		}
		else {
			userService.saveUserwithDefaultRole(user);
			return "redirect:/signup?success";
		}
	}
	
	@GetMapping("/home")
	public String displayUserHomePage(Model model) {
		return "/user/home";
	}
	
	@GetMapping("/dashboard")
	public String displayUserDashboard(Model model, Principal principal, HttpServletRequest request) {
		User user = userService.findUsernameByPrincipalEmail(principal.getName());
		List<User> listUser = userService.findUserList(); 
		List<Project> listProject = projectService.findAllCreatedProject();
		
		if(user.hasRole("Admin")) {
			Integer newStatus = ticketService.findTicketCountByTicketStatus("New");
			Integer inProgressStatus = ticketService.findTicketCountByTicketStatus("In Progress");
			Integer resolvedStatus = ticketService.findTicketCountByTicketStatus("Resolved");
		
			Integer lowPriority = ticketService.findTicketCountByTicketPriority("Low");
			Integer mediumPriority = ticketService.findTicketCountByTicketPriority("Medium");
			Integer highPriority = ticketService.findTicketCountByTicketPriority("High");
			Integer immediatePriority = ticketService.findTicketCountByTicketPriority("Immediate");
			
			Integer issueType = ticketService.findTicketCountByTicketType("Issue");
			Integer featureRequestType = ticketService.findTicketCountByTicketType("Feature Request");
			Integer bugType = ticketService.findTicketCountByTicketType("Bug");
			
			model.addAttribute("newStatus",newStatus);
			model.addAttribute("inProgressStatus",inProgressStatus);
			model.addAttribute("resolvedStatus",resolvedStatus);
			model.addAttribute("lowPriority",lowPriority);
			model.addAttribute("mediumPriority",mediumPriority);
			model.addAttribute("highPriority",highPriority);
			model.addAttribute("immediatePriority",immediatePriority);
			model.addAttribute("issueType",issueType);
			model.addAttribute("bugType",bugType);
			model.addAttribute("featureRequestType",featureRequestType);
			model.addAttribute("listProject", listProject);
			
		}else if(user.hasRole("User")) {
			Long projectId = user.getProject().getId();
			Integer newStatus = ticketService.findTicketStatusCountByProjectId(projectId, "New");
			Integer inProgressStatus = ticketService.findTicketStatusCountByProjectId(projectId, "In Progress");
			Integer resolvedStatus = ticketService.findTicketStatusCountByProjectId(projectId, "Resolved");
		
			Integer lowPriority = ticketService.findTicketPriorityCountByProjectId(projectId, "Low");
			Integer mediumPriority = ticketService.findTicketPriorityCountByProjectId(projectId, "Medium");
			Integer highPriority = ticketService.findTicketPriorityCountByProjectId(projectId, "High");
			Integer immediatePriority = ticketService.findTicketPriorityCountByProjectId(projectId, "Immediate");
			
			Integer issueType = ticketService.findTicketTypeCountByProjectId(projectId, "Issue");
			Integer featureRequestType = ticketService.findTicketTypeCountByProjectId(projectId, "Feature Request");
			Integer bugType = ticketService.findTicketTypeCountByProjectId(projectId, "Bug");
			
			model.addAttribute("newStatus",newStatus);
			model.addAttribute("inProgressStatus",inProgressStatus);
			model.addAttribute("resolvedStatus",resolvedStatus);
			model.addAttribute("lowPriority",lowPriority);
			model.addAttribute("mediumPriority",mediumPriority);
			model.addAttribute("highPriority",highPriority);
			model.addAttribute("immediatePriority",immediatePriority);
			model.addAttribute("issueType",issueType);
			model.addAttribute("bugType",bugType);
			model.addAttribute("featureRequestType",featureRequestType);
			model.addAttribute("listProject", user.getProject());
		}
		model.addAttribute("project", new Project());
		model.addAttribute("listUser", listUser);
		model.addAttribute("username",user.getUsername());

		
		return "/user/dashboard";
	}
	
	@GetMapping("/project/{projectId}")
	public String displaySelectedProjectDetailsFromDashboard(Model model, @PathVariable(name="projectId") Long projectId, Principal principal) {
		Project foundProject = projectService.findProjectById(projectId);
		String foundProjectName = foundProject.getProjectName();
		Set<User> contributorsSet = foundProject.getContributors();
		List<User> listAllUser = userService.findUserList();
		User user = userService.findUsernameByPrincipalEmail(principal.getName());
		List<Ticket> listTicket = ticketService.findAllProjectTickets();
		String currentLoggedInUsername = user.getUsername();
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss a , E");  
		LocalDateTime now = LocalDateTime.now();    
		
		String formattedDateTime = now.format(dtf);
		
		//convert set <user> to list string
		List<User> listUser = contributorsSet
				.stream()
				.collect(Collectors.toList());
		
		model.addAttribute("currentLoggedInUsername",currentLoggedInUsername);
		model.addAttribute("dateTime", formattedDateTime);
		model.addAttribute("projectName",foundProjectName);
		model.addAttribute("projectId",projectId);
		model.addAttribute("listUser", listUser);
		model.addAttribute("listAllUser",listAllUser);
		model.addAttribute("username",user.getUsername());
		model.addAttribute("ticket", new Ticket());
		model.addAttribute("listTicket",listTicket);
		
		
		return "/user/projectDetails";
	}
	
	@PostMapping("/saveNewProject")
	public String addNewProject(Model model, HttpServletRequest request, Project project) {
		//Retrieve the list of contributors
		String[] contributorParams = request.getParameterValues("contributors");
	
		for(int i = 0; i<contributorParams.length;i++) {
			//for the retrieved list of contributors, find its user name with its id
			User foundUser = userService.findUserByUserId(Long.parseLong(contributorParams[i]));
			//System.out.println(foundUser.getUsername());
			projectService.saveProjectDetails(project, foundUser);
		}
		
		model.addAttribute("project", new Project());
		
		return "redirect:/dashboard?success";
	}
	
	@PostMapping("/saveNewTicket")
	public String addNewTicket(Model model,  HttpServletRequest request, Ticket ticket,@RequestParam(value = "project")String project) {
		//Retrieve the list of developers
		String[] developersParams = request.getParameterValues("assignedDevs");
		
		for(int i=0; i<developersParams.length;i++) {
			//for the retrieved list of developers, find its username with its id;
			User foundDev = userService.findUserByUserId(Long.parseLong(developersParams[i]));
			ticketService.saveTicketDetails(ticket, Long.parseLong(project), foundDev);
		}
		
		List<Ticket> listTicket = ticketService.findAllProjectTickets();
		
		model.addAttribute("ticket", new Ticket());
		model.addAttribute("listTicket",listTicket);
		
		return "redirect:/project/"+project;
	}
	
	@GetMapping("/ticket")
	public String displayTicketPage(Model model, Ticket ticket,Principal principal) {
		model.addAttribute("ticket", new Ticket());
		return findPaginated(1,model,principal);
	}
	
	@GetMapping("/ticket/{pageNo}")
	public String findPaginated(@PathVariable(value="pageNo") int pageNo,Model model,Principal principal){
		int pageSize = 3;
		
		User user = userService.findUsernameByPrincipalEmail(principal.getName());
		String authorName = user.getUsername();
		
		System.out.println(authorName);
		
		//Paging all the ticket data
		Page<Ticket> page = ticketService.findPaginated(pageNo, pageSize);
		List<Ticket> listTicket = page.getContent();
		
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("pageNo",pageNo);		
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listTicket",listTicket);
		model.addAttribute("ticket", new Ticket());
		
		return "user/ticket";
	}
	
	@GetMapping("/updateProject")
	public String editProjectForUpdate(Project project, HttpServletRequest request){
		String projectName = request.getParameter("projectName"); //Retrieve the content of project name field
		String description  = request.getParameter("description"); //Retrieve the content of project description field
		String projectId = request.getParameter("projectId"); //Retrieve the project id
	
		projectService.updateProjectEditDetails(Long.parseLong(projectId), projectName, description);

		return "redirect:/dashboard"; 
	}
	
	@GetMapping("/updateTicket")
	public String editProjectTicketForUpdate(Project project, Ticket ticket, HttpServletRequest request, @RequestParam(value = "project")String projectId) {
		String ticketId = request.getParameter("ticketId");
		String ticketTitle = request.getParameter("ticketTitle");
		String description = request.getParameter("description");
		String author = request.getParameter("author");
		String status = request.getParameter("status");
		String priority = request.getParameter("priority");
		String ticketType = request.getParameter("ticketType");
		
		ticketService.updateTicketEditDetails(Long.parseLong(ticketId), ticketTitle, description, author, status, priority, ticketType);
		
		System.out.println(projectId);
		System.out.println(ticketId);
		System.out.println(ticketTitle);
		System.out.println(description);
		System.out.println(author);
		System.out.println(status);
		System.out.println(priority);
		System.out.println(ticketType);
		
		return "redirect:/project/"+projectId;
	}
	
	@GetMapping("/addTeamMember")
	public String addTeamMemberIntoExistingProjectTeam(Project project, HttpServletRequest request,@RequestParam(value = "project")String projectId){
		String [] projectTeamMemberParams = request.getParameterValues("contributors");
		
		System.out.println("Project Id (Selected): " + Long.parseLong(projectId));
		for(int i=0;i<projectTeamMemberParams.length;i++) {
			System.out.println("Selected Contributor Id: " + projectTeamMemberParams[i]);
			User foundUser = userService.findUserByUserId(Long.parseLong(projectTeamMemberParams[i]));
			projectService.addTeamMembers(Long.parseLong(projectId), foundUser);
		}
		
		return "redirect:/project/"+projectId;
	}
	
	@GetMapping("/deleteTeamMember")
	public String deletedSelectedTeamMemberFromProjectTeam(Project project, HttpServletRequest request,@RequestParam(value = "project")String projectId){
		String userId = request.getParameter("userId");
		User foundUser = userService.findUserByUserId(Long.parseLong(userId));
		projectService.deleteTeamMembersFromProjectTeam(foundUser);
		return "redirect:/project/"+projectId;
	}
	
	@RequestMapping("/getOneTicket")
	@ResponseBody
	public Optional<Ticket> getOneTicket(Long id){
		return ticketService.getOneTicket(id);
	}
	
	@GetMapping("/deleteProject/{id}")
	public String deleteProject(@PathVariable(name="id")Long id ){
		projectService.deleteSelectedProject(id);
		return "redirect:/dashboard";
	}
	
	@GetMapping("/deleteTicket/{id}")
	public String deleteTicket(@PathVariable(name="id")Long id){
		ticketService.deleteSelectedTicket(id);
		return "redirect:/dashboard";
	}
	
	
	
	
}
