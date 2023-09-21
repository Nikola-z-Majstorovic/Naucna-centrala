package org.camunda.bpmn.dto;

public class AccessAndMembershipDTO {

    private boolean openAccess;
    private boolean membership;

    public AccessAndMembershipDTO() {
    }

    public AccessAndMembershipDTO(boolean openAccess, boolean membership) {
        this.openAccess = openAccess;
        this.membership = membership;
    }

    public boolean isOpenAccess() {
        return openAccess;
    }

    public void setOpenAccess(boolean openAccess) {
        this.openAccess = openAccess;
    }

    public boolean isMembership() {
        return membership;
    }

    public void setMembership(boolean membership) {
        this.membership = membership;
    }
}
