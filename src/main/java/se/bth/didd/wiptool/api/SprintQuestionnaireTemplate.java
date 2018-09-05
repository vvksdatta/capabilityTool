package se.bth.didd.wiptool.api;

import java.util.Date;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;

public class SprintQuestionnaireTemplate {

	@NotNull
	@JsonProperty
	@ColumnName("projectId")
	public Integer projectId;

	@JsonProperty
	@ColumnName("sprintEstimatedEffort")
	public Integer sprintEstimatedEffort;

	@JsonProperty
	@ColumnName("sprintActualEffort")
	public Integer sprintActualEffort;

	@NotNull
	@JsonProperty
	@ColumnName("sprintId")
	public Integer sprintId;

	@JsonProperty
	@ColumnName("question1")
	public String question1;

	@JsonProperty
	@ColumnName("question2")
	public String question2;

	@JsonProperty
	@ColumnName("question3")
	public String question3;

	@JsonProperty
	@ColumnName("question4")
	public String question4;

	@JsonProperty
	@ColumnName("question5")
	public String question5;

	@JsonProperty
	@ColumnName("question6")
	public String question6;

	@JsonProperty
	@ColumnName("question7")
	public String question7;

	@JsonProperty
	@ColumnName("question8")
	public String question8;

	@JsonProperty
	@ColumnName("question1Comment")
	public String question1Comment;

	@JsonProperty
	@ColumnName("question2Comment")
	public String question2Comment;

	@JsonProperty
	@ColumnName("question3Comment")
	public String question3Comment;

	@JsonProperty
	@ColumnName("question4Comment")
	public String question4Comment;

	@JsonProperty
	@ColumnName("question5Comment")
	public String question5Comment;

	@JsonProperty
	@ColumnName("question6Comment")
	public String question6Comment;

	@JsonProperty
	@ColumnName("question7Comment")
	public String question7Comment;

	@JsonProperty
	@ColumnName("question8Comment")
	public String question8Comment;

	@JsonProperty
	@ColumnName("other")
	public String other;

	@JsonProperty
	@ColumnName("updatedBy")
	public String updatedBy;

	@JsonProperty
	@ColumnName("lastUpdate")
	public Date lastUpdate;

	public SprintQuestionnaireTemplate() {

	}

	public SprintQuestionnaireTemplate(Integer projectId, Integer sprintEstimatedEffort, Integer sprintActualEffort,
			Integer sprintId, String question1, String question2, String question3, String question4, String question5,
			String question6, String question7, String question8, String question1Comment, String question2Comment,
			String question3Comment, String question4Comment, String question5Comment, String question6Comment,
			String question7Comment, String question8Comment, String other, String updatedBy, Date lastUpdate) {
		super();
		this.projectId = projectId;
		this.sprintEstimatedEffort = sprintEstimatedEffort;
		this.sprintActualEffort = sprintActualEffort;
		this.sprintId = sprintId;
		this.question1 = question1;
		this.question2 = question2;
		this.question3 = question3;
		this.question4 = question4;
		this.question5 = question5;
		this.question6 = question6;
		this.question7 = question7;
		this.question8 = question8;
		this.question1Comment = question1Comment;
		this.question2Comment = question2Comment;
		this.question3Comment = question3Comment;
		this.question4Comment = question4Comment;
		this.question5Comment = question5Comment;
		this.question6Comment = question6Comment;
		this.question7Comment = question7Comment;
		this.question8Comment = question8Comment;
		this.other = other;
		this.updatedBy = updatedBy;
		this.lastUpdate = lastUpdate;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public Integer getSprintId() {
		return sprintId;
	}

	public String getQuestion1() {
		return question1;
	}

	public String getQuestion2() {
		return question2;
	}

	public String getQuestion3() {
		return question3;
	}

	public String getQuestion4() {
		return question4;
	}

	public String getOther() {
		return other;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public void setSprintId(Integer sprintId) {
		this.sprintId = sprintId;
	}

	public void setQuestion1(String question1) {
		this.question1 = question1;
	}

	public void setQuestion2(String question2) {
		this.question2 = question2;
	}

	public void setQuestion3(String question3) {
		this.question3 = question3;
	}

	public void setQuestion4(String question4) {
		this.question4 = question4;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Integer getSprintEstimatedEffort() {
		return sprintEstimatedEffort;
	}

	public Integer getSprintActualEffort() {
		return sprintActualEffort;
	}

	public void setSprintEstimatedEffort(Integer sprintEstimatedEffort) {
		this.sprintEstimatedEffort = sprintEstimatedEffort;
	}

	public void setSprintActualEffort(Integer sprintActualEffort) {
		this.sprintActualEffort = sprintActualEffort;
	}

	public String getQuestion5() {
		return question5;
	}

	public String getQuestion6() {
		return question6;
	}

	public String getQuestion7() {
		return question7;
	}

	public String getQuestion8() {
		return question8;
	}

	public void setQuestion5(String question5) {
		this.question5 = question5;
	}

	public void setQuestion6(String question6) {
		this.question6 = question6;
	}

	public void setQuestion7(String question7) {
		this.question7 = question7;
	}

	public void setQuestion8(String question8) {
		this.question8 = question8;
	}

	public String getQuestion1Comment() {
		return question1Comment;
	}

	public String getQuestion2Comment() {
		return question2Comment;
	}

	public String getQuestion3Comment() {
		return question3Comment;
	}

	public String getQuestion4Comment() {
		return question4Comment;
	}

	public String getQuestion5Comment() {
		return question5Comment;
	}

	public String getQuestion6Comment() {
		return question6Comment;
	}

	public String getQuestion7Comment() {
		return question7Comment;
	}

	public String getQuestion8Comment() {
		return question8Comment;
	}

	public void setQuestion1Comment(String question1Comment) {
		this.question1Comment = question1Comment;
	}

	public void setQuestion2Comment(String question2Comment) {
		this.question2Comment = question2Comment;
	}

	public void setQuestion3Comment(String question3Comment) {
		this.question3Comment = question3Comment;
	}

	public void setQuestion4Comment(String question4Comment) {
		this.question4Comment = question4Comment;
	}

	public void setQuestion5Comment(String question5Comment) {
		this.question5Comment = question5Comment;
	}

	public void setQuestion6Comment(String question6Comment) {
		this.question6Comment = question6Comment;
	}

	public void setQuestion7Comment(String question7Comment) {
		this.question7Comment = question7Comment;
	}

	public void setQuestion8Comment(String question8Comment) {
		this.question8Comment = question8Comment;
	}

}
