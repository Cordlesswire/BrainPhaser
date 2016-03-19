package de.fhdw.ergoholics.brainphaser.database;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.fhdw.ergoholics.brainphaser.model.Challenge;
import de.fhdw.ergoholics.brainphaser.model.ChallengeDao;
import de.fhdw.ergoholics.brainphaser.model.DaoSession;
import de.fhdw.ergoholics.brainphaser.model.User;

/**
 * Created by Daniel Hoogen on 25/02/2016.
 * <p/>
 * Data Source class for custom access to challenge table entries in the database
 */
public class ChallengeDataSource {
    //Attributes
    private DaoSession mDaoSession;
    private CompletionDataSource mCompletionDataSource;

    /**
     * Constructor which saves all given parameters to local member attributes.
     *
     * @param session              the session to be saved as a member attribute
     * @param completionDataSource the completion data source to be saved as a member attribute
     */
    @Inject
    ChallengeDataSource(DaoSession session, CompletionDataSource completionDataSource) {
        mDaoSession = session;
        mCompletionDataSource = completionDataSource;
    }

    /**
     * Returns a list with all challenges
     *
     * @return list with all challenges
     */
    public List<Challenge> getAll() {
        return mDaoSession.getChallengeDao().loadAll();
    }

    /**
     * Returns the Challenge object with the given id
     *
     * @param id challenge id in the challenge table
     * @return Challenge object with the given id
     */
    public Challenge getById(long id) {
        return mDaoSession.getChallengeDao().load(id);
    }

    /**
     * Adds a Challenge object to the database
     *
     * @param challenge challenge to be created in the challenge table
     * @return id of the created object
     */
    public long create(Challenge challenge) {
        return mDaoSession.getChallengeDao().insert(challenge);
    }

    /**
     * Returns all challenges that have never been completed by the given user before
     *
     * @param user the user whose not completed challenges will be returned
     * @return list of uncompleted challenges
     */
    public List<Challenge> getUncompletedChallenges(User user) {
        List<Challenge> notCompleted = new ArrayList<>();
        long userId = user.getId();

        List<Challenge> challenges = mDaoSession.getChallengeDao().queryBuilder().list();

        for (Challenge challenge : challenges) {
            if (mCompletionDataSource.findByChallengeAndUser(challenge.getId(), userId) == null) {
                notCompleted.add(challenge);
            }
        }

        return notCompleted;
    }

    /**
     * Returns all challenges with the given category id
     *
     * @param categoryId the category id of the category whose challenges are returned
     * @return list of challenges with the given category id
     */
    public List<Challenge> getByCategoryId(long categoryId) {
        if (categoryId == CategoryDataSource.CATEGORY_ID_ALL) {
            return getAll();
        } else {
            return mDaoSession.getChallengeDao().queryBuilder()
                    .where(ChallengeDao.Properties.CategoryId.eq(categoryId)).list();
        }
    }
}