package com.example.util;

import com.example.po.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * lxl
 * User: Appchina
 * Date: 13-8-26
 * Time: ����2:23
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
     * ���ĳ��Ƭ��������ʹ��״̬
     * @param cd
     * @param used
     */
    public void addCard(Card cd,boolean used){
        int i=0;
        for(;i<cards.size();i++){
            Card thisCard = cards.get(i);
            if(thisCard.getId().equals(cd.getId())){        //Ѱ�Ҽ�¼�еĿ�Ƭ
                 isUsed.set(i,used);
                 return;
            }
        }
        cards.add(cd);
        isUsed.add(used);
    }

    /**
     * ���ÿ�ƬΪ����
     * @param cd
     * @param used
     */
    public void setCardUsed(Card cd,boolean used){
        int i=0;
        for(;i<cards.size();i++){
            Card thisCard = cards.get(i);
            if(thisCard.getId().equals(cd.getId())){        //Ѱ�Ҽ�¼�еĿ�Ƭ
                isUsed.set(i,used);
            }
        }
    }

    /**
     * �жϿ�Ƭ�Ƿ�ʹ��
     * @param cd
     * @return
     */
    public boolean isCardUsed(Card cd){
        int i=0;
        for(;i<cards.size();i++){
            Card thisCard = cards.get(i);
            if(thisCard.getId().equals(cd.getId())){        //Ѱ�Ҽ�¼�еĿ�Ƭ
                return isUsed.get(i);
            }
        }
        return false;
    }
}
