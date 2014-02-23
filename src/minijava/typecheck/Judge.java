package minijava.typecheck;

import minijava.symboltable.MArray;
import minijava.symboltable.MBoolean;
import minijava.symboltable.MClass;
import minijava.symboltable.MClasses;
import minijava.symboltable.MInteger;
import minijava.symboltable.MMethod;
import minijava.symboltable.MType;
import Mainclass.Mainclass;

public class Judge {
    public static boolean isTypeMatched(MType left, MType right) {
    	if (left.getType().equals(right.getType()))
    		return true;
    	else {
    		if (right.getType() != MInteger.type && right.getType() != MBoolean.type && right.getType() != MArray.type) {
    			MClass father = ((MClasses)Mainclass.my_classes).getClassByName(right.getType()).getFather();
    			while (father != null) {
    				if (left.getType() == father.getName())
    					return true;
    				father = father.getFather();
    			}
    		}
			return false;
    	}
    }
    public static MType isVarDeclared(String target, MType argu) {
    	MType _ret = ((MMethod)argu).getVarByName(target);
    	if (_ret == null)
    		_ret = ((MClass)argu.getField()).getVarByName(target);
    	if (target.equals("this"))
    		return _ret;
    	if (_ret == null) {
    		MClass father = ((MClass)argu.getField()).getFather();
    		while (father != null) {
    			_ret = father.getVarByName(target);
    			if (_ret != null)
    				return _ret;
    			father = father.getFather();
    		}
    	}
    	return _ret;
    }
    public static MType isMethodDeclared(String target, MType argu) {
    	MType method = ((MClass)argu).getMethodByName(target);
    	
    	if (method == null) {
    		MClass father = ((MClass)argu).getFather();
    		while (father != null) {
    			method = father.getMethodByName(target);
    			if (method != null)
    				return method;
    			father = father.getFather();
    		}
    	}
    	return method;
    }
}
