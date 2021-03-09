package cho.carbon.auth.pojo;

import cho.carbon.auth.common.UserUtils;
import cho.carbon.bond.pojo.criteria.LevelTwoRelationCriteriaValue;
import cho.carbon.bond.pojo.criteria.NormalCriteria;
import cho.carbon.bond.pojo.criteria.conveter.CriteriaConverter;
import cho.carbon.bond.utils.TextUtils;
import cho.carbon.query.entity.factory.EnRightRelationCriterionFactory;
import cho.carbon.query.entity.factory.EnRightRelationJunctionFactory;
import cho.carbon.query.entity.factory.EntityConJunctionFactory;

public class Level2UserRelationExistCriteriaConverter implements CriteriaConverter {

    @Override
    public boolean support(NormalCriteria nCriteria) {
        LevelTwoRelationCriteriaValue value = new LevelTwoRelationCriteriaValue(nCriteria.getValue());
        return value.hasLevelTwo() && TextUtils.hasText(nCriteria.getMSFieldGroupId()) &&
                NormalCriteria.isExistRelationComparator(nCriteria.getComparator());
    }

    @Override
    public void invokeAddCriteria(EntityConJunctionFactory conjunctionFactory,
                                  NormalCriteria nCriteria) {
        if (nCriteria.getMStrucFieldGroup() == null || !TextUtils.hasText(nCriteria.getValue())) {
            return;
        }
		LevelTwoRelationCriteriaValue value = new LevelTwoRelationCriteriaValue(nCriteria.getValue());
        String fieldGroupName = nCriteria.getMStrucFieldGroup().getTitle();
        EnRightRelationJunctionFactory rightCriteriaFactory = conjunctionFactory.getRighterCriteriaFactory(fieldGroupName);
        EnRightRelationCriterionFactory factory = rightCriteriaFactory.getRightRelationCriterionFactory();
        String oneLevelValue=value.getOneLevelValue();
        //添加1leve
        if ("re2".equals(nCriteria.getComparator())) {
            factory.setInRelationTypes(oneLevelValue);
            String userCode = UserUtils.getCurrentUser().getUserInfo().getCode();
            factory.setInRightCodes(userCode);
        } else if ("re1".equals(nCriteria.getComparator())) {
            factory.setInRelationTypes(oneLevelValue);
        } else {
            factory.setExRelationTypes(oneLevelValue);
        }
        //添加2level
		EnRightRelationCriterionFactory factory2 = factory.getRightJunctionFactory().getRighterCriteriaFactory(value.getLevel2MStrucFieldGroup().getTitle()).getRightRelationCriterionFactory();
		String twoLevelValue=value.getTwoLevelValue();
		if ("re2".equals(value.getTwoLevelComparator())) {
			factory2.setInRelationTypes(twoLevelValue);
			String userCode = UserUtils.getCurrentUser().getUserInfo().getCode();
			factory2.setInRightCodes(userCode);
		} else if ("re1".equals(value.getTwoLevelComparator())) {
			factory2.setInRelationTypes(twoLevelValue);
		} else {
			factory2.setExRelationTypes(twoLevelValue);
		}

    }


}
