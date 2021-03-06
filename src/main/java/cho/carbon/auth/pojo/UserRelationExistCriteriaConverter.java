package cho.carbon.auth.pojo;

import javax.annotation.Resource;

import cho.carbon.auth.common.UserUtils;
import cho.carbon.bond.pojo.criteria.NormalCriteria;
import cho.carbon.bond.pojo.criteria.conveter.CriteriaConverter;
import cho.carbon.bond.utils.TextUtils;
import cho.carbon.query.entity.factory.EnRightRelationJunctionFactory;
import cho.carbon.query.entity.factory.EntityConJunctionFactory;

public class UserRelationExistCriteriaConverter implements CriteriaConverter{

	public static interface UserCodeSupplier {
		String getUserCode();
	}
	
	@Override
	public boolean support(NormalCriteria nCriteria) {
		return  nCriteria.getMSFieldGroupId() != null && "re2".equals(nCriteria.getComparator());
	}

	@Override
	public void invokeAddCriteria(EntityConJunctionFactory conjunctionFactory,
			NormalCriteria nCriteria) {
		if(nCriteria.getMStrucFieldGroup() != null && TextUtils.hasText(nCriteria.getValue())) {
			String fieldGroupName = nCriteria.getMStrucFieldGroup().getTitle();
			
			EnRightRelationJunctionFactory rightCriteriaFactory = conjunctionFactory.getRighterCriteriaFactory(fieldGroupName);
			rightCriteriaFactory.getRightRelationCriterionFactory().setInRelationTypes(nCriteria.getValue());
			String userCode = UserUtils.getCurrentUser().getUserInfo().getCode();
			rightCriteriaFactory.getRightRelationCriterionFactory().setInRightCodes(userCode);
			
		}
	}
	
}
