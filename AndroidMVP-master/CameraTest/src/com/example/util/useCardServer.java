package com.example.util;

import com.example.po.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * lxl
 * User: Appchina
 * Date: 13-8-26
 * Time: 下午2:23
 * To change this template use File | Settings | File Templates.
 */
public class useCardServer {
    List<Card> cards;
    List<Boolean> isUsed;
    public useCardServer(){
        cards = new ArrayList<Card>();
        isUsed = new ArrayList<Boolean>();
    }

    /**
     * 添加某卡片并且设置使用状态
     * @param cd
     * @param used
     */
    public void addCard(Card cd,boolean used){
        int i=0;
        for(;i<cards.size();i++){
            Card thisCard = cards.get(i);
            if(thisCard.getId().equals(cd.getId())){        //寻找记录中的卡片
                 isUsed.set(i,used);
                 return;
            }
        }
        cards.add(cd);
        isUsed.add(used);
    }

    /**
     * 设置卡片为已用
     * @param cd
     * @param used
     */
    public void setCardUsed(Card cd,boolean used){
        int i=0;
        for(;i<cards.size();i++){
            Card thisCard = cards.get(i);
            if(thisCard.getId().equals(cd.getId())){        //寻找记录中的卡片
                isUsed.set(i,used);
            }
        }
    }

    /**
     * 判断卡片是否使用
     * @param cd
     * @return
     */
    public boolean isCardUsed(Card cd){
        int i=0;
        for(;i<cards.size();i++){
            Card thisCard = cards.get(i);
            if(thisCard.getId().equals(cd.getId())){        //寻找记录中的卡片
                return isUsed.get(i);
            }
        }
        return false;
    }
}
