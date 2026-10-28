package org.group5.service;

public class MemberService {
    
     public void addMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member details cannot be null.");
        }
        if (member.getMemberId() == null || member.getMemberId().trim().isEmpty()) {
            throw new IllegalArgumentException("Member ID cannot be empty.");
        }
        if (member.getName() == null || member.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Member name cannot be empty.");
        }
        if (findMemberById(member.getMemberId().trim()) != null) {
            throw new IllegalArgumentException("A member with ID '" + member.getMemberId().trim() + "' already exists.");
        }

        member.setMemberId(member.getMemberId().trim());
        member.setName(member.getName().trim());
        member.setContactNumber(member.getContactNumber() != null ? member.getContactNumber().trim() : "-");

        members.add(member);
    }

    

    public boolean removeMember(String memberId) {
        if (memberId == null || memberId.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter a valid Member ID to remove.");
        }
        Member member = findMemberById(memberId.trim());
        if (member == null) {
            throw new IllegalArgumentException("Member with ID '" + memberId.trim() + "' not found.");
        }
        // Check if member has active borrowed books
        for (BorrowRecord record : borrowRecords) {
            if (record.isActive() && record.getMember().getMemberId().equalsIgnoreCase(member.getMemberId())) {
                throw new IllegalArgumentException("Cannot remove Member '" + member.getName() + "' because they currently have borrowed books.");
            }
        }
        return members.remove(member);
    }

    

    public List<Member> searchMembers(String keyword) {
        List<Member> results = new ArrayList<Member>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<Member>(members);
        }
        String lowerKey = keyword.trim().toLowerCase();
        for (Member m : members) {
            if (m.getMemberId().toLowerCase().contains(lowerKey) ||
                m.getName().toLowerCase().contains(lowerKey) ||
                m.getContactNumber().toLowerCase().contains(lowerKey)) {
                results.add(m);
            }
        }
        return results;
    }

    

    public Member findMemberById(String memberId) {
        if (memberId == null) return null;
        for (Member m : members) {
            if (m.getMemberId().equalsIgnoreCase(memberId.trim())) {
                return m;
            }
        }
        return null;
    }

    public List<Member> getAllMembers() {
        return new ArrayList<Member>(members);
    }
}
