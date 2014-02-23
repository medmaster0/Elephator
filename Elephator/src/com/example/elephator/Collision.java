package com.example.elephator;

import com.example.elephator.Creature;
import com.example.elephator.Product;
import com.example.elephator.Egg;
import com.example.elephator.Platform;

public class Collision {
	
	public static boolean creaturePlatformCollision(Creature cre, Platform plat){
		if(((cre.y + (1.5*cre.r)) >= plat.y) && (cre.y <= plat.y)){
			if((cre.x > plat.x) && ((cre.x + cre.r) < (plat.x + plat.length))){
				return true;				
			}			
		}
		
		return false;
	}
	
	public static boolean creatureProductCollision(Creature cre, Product pro){
		if( (Math.pow((cre.getxPos() - pro.getX()), 2) 
				+ Math.pow((cre.getyPos() - pro.getY()),2))
				< Math.pow((cre.getR() + pro.getR()),2) ){
			return true;
		}else{
			return false;
		}
		
	}
	
	public static boolean eggPlatformCollision(Egg egg, Platform plat){
		if(((egg.y + egg.r) > plat.y) && (egg.y < plat.y)){
			if((egg.x > plat.x) && ((egg.x + egg.r) < (plat.x + plat.length))){
				return true;				
			}			
		}	
		return false;
	}
	
	
}
