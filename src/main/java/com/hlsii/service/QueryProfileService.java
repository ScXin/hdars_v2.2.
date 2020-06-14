package com.hlsii.service;
//import com.hlsii.dao.ProfileGroupDao;
//import com.hlsii.dao.QueryProfileDao;
/**
 * User query profile
 *
 */
//
////extends CrudService<QueryProfile, QueryProfileDao>
//@Service
//@Transactional(readOnly = true)
//public class QueryProfileService  {
//    @Autowired
//    private ProfileGroupDao profileGroupDao;
//
//    @Autowired
//    private QueryProfileDao queryProfileDao;
//    /**
//     * Get user query profile by user id
//     *
//     * @param userId - user id
//     * @return - user query profile
//     */
//    public List<QueryProfile> getUserProfiles(String userId) {
//        return queryProfileDao.getUserProfiles(userId);
//    }
//
//    /**
//     * Get all profile groups by profile Id
//     *
//     * @param profId - Profile id
//     * @return - all profile groups
//     */
//    public List<ProfileGroup> getProfileGroups(String profId) {
//        return profileGroupDao.getProfileGroups(profId);
//    }
//
//    /**
//     * Save user query profile
//     *
//     * @param profile - user profile
//     */
//    @Transactional(readOnly = false)
//    public QueryProfile saveProfile(QueryProfile profile, List<ProfileGroup> groups) {
//        // Find out existing group for that profile
//        List<String> existingGroups = new ArrayList<>();
//        if (!StringUtils.isEmpty(profile.getId())) {
//            for(ProfileGroup group : profileGroupDao.getProfileGroups(profile.getId())) {
//                existingGroups.add(group.getId());
////                queryProfileDao.evict(group.getProfile());
////                profileGroupDao.evict(group);
//            }
//        }
//
//        // Update the last update time before save.
//        profile.setUpdateTime(new Date());
//        queryProfileDao.saveOrUpdate(profile);
//
//        // Save or update each group
//        Set<String> newGroupIdMap = new HashSet<>();
//        for(ProfileGroup group : groups) {
//            group.setProfile(profile);
//            profileGroupDao.saveOrUpdate(group);
//            newGroupIdMap.add(group.getId());
//        }
//
//        // Delete the not used group from the profile
//        for(String groupId : existingGroups) {
//            if (!newGroupIdMap.contains(groupId)) {
//                profileGroupDao.delete(groupId);
//            }
//        }
//        return profile;
//    }
//
//    /**
//     * Delete user query profile
//     *
//     * @param profile - user profile
//     */
//    @Transactional(readOnly = false)
//    public void deleteProfile(QueryProfile profile) {
//        for(ProfileGroup group : profileGroupDao.getProfileGroups(profile.getId())) {
//            profileGroupDao.delete(group);
//        }
//        profileGroupDao.delete(profile);
//    }
//
//
//    public QueryProfile get(String profId){
//        return queryProfileDao.getProfileById(profId);
//    }
//}