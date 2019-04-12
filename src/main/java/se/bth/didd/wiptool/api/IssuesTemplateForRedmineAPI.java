package se.bth.didd.wiptool.api;

import java.util.Date;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Template to fetching and updating the details of an issue.
 */

public class IssuesTemplateForRedmineAPI {
	@JsonProperty
	@NotNull
	public Integer id;

	@JsonProperty
	@NotNull
	public Status project;

	@JsonProperty
	@NotNull
	public Status tracker;

	@JsonProperty
	@NotNull
	public Status status;

	@JsonProperty
	@NotNull
	public Status assigned_to;

	@JsonProperty
	@NotNull
	public Status priority;

	@JsonProperty
	@NotNull
	public Status author;

	@JsonProperty
	@NotNull
	public Status category;

	@JsonProperty
	@NotNull
	public Status fixed_version;

	@JsonProperty
	@NotNull
	public String subject;

	@JsonProperty
	@NotNull
	public String description;

	@JsonProperty
	@NotNull
	public Date start_date;

	@JsonProperty
	@NotNull
	public Date due_date;

	@JsonProperty
	@NotNull
	public int done_ratio;

	@JsonProperty
	@NotNull
	public Float estimated_hours;

	@JsonProperty
	@NotNull
	public Date created_on;

	@JsonProperty
	@NotNull
	public Date updated_on;

	public IssuesTemplateForRedmineAPI(Integer id, Status project, Status tracker, Status status, Status assigned_to,
			Status priority, Status author, Status category, Status fixed_version, String subject, String description,
			Date start_date, Date due_date, int done_ratio, Float estimated_hours, Date created_on, Date updated_on) {
		super();
		this.id = id;
		this.project = project;
		this.tracker = tracker;
		this.status = status;
		this.assigned_to = assigned_to;
		this.priority = priority;
		this.author = author;
		this.category = category;
		this.fixed_version = fixed_version;
		this.subject = subject;
		this.description = description;
		this.start_date = start_date;
		this.due_date = due_date;
		this.done_ratio = done_ratio;
		this.estimated_hours = estimated_hours;
		this.created_on = created_on;
		this.updated_on = updated_on;
	}

	public IssuesTemplateForRedmineAPI() {

	}

	public Integer getId() {
		return id;
	}

	public Status getProject() {
		return project;
	}

	public Status getTracker() {
		return tracker;
	}

	public Status getStatus() {
		return status;
	}

	public Status getPriority() {
		return priority;
	}

	public Status getAuthor() {
		return author;
	}

	public Status getCategory() {
		return category;
	}

	public Status getFixed_version() {
		return fixed_version;
	}

	public String getSubject() {
		return subject;
	}

	public String getDescription() {
		return description;
	}

	public Date getStart_date() {
		return start_date;
	}

	public int getDone_ratio() {
		return done_ratio;
	}

	public Float getEstimated_hours() {
		return estimated_hours;
	}

	public Date getCreated_on() {
		return created_on;
	}

	public Date getUpdated_on() {
		return updated_on;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setProject(Status project) {
		this.project = project;
	}

	public void setTracker(Status tracker) {
		this.tracker = tracker;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setPriority(Status priority) {
		this.priority = priority;
	}

	public void setAuthor(Status author) {
		this.author = author;
	}

	public void setCategory(Status category) {
		this.category = category;
	}

	public void setFixed_version(Status fixed_version) {
		this.fixed_version = fixed_version;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public void setDone_ratio(int done_ratio) {
		this.done_ratio = done_ratio;
	}

	public void setEstimated_hours(Float estimated_hours) {
		this.estimated_hours = estimated_hours;
	}

	public void setCreated_on(Date created_on) {
		this.created_on = created_on;
	}

	public void setUpdated_on(Date updated_on) {
		this.updated_on = updated_on;
	}

	public Date getDue_date() {
		return due_date;
	}

	public void setDue_date(Date due_date) {
		this.due_date = due_date;
	}

	public Status getAssigned_to() {
		return assigned_to;
	}

	public void setAssigned_to(Status assigned_to) {
		this.assigned_to = assigned_to;
	}

}
