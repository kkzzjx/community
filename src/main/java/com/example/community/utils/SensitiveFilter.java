package com.example.community.utils;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: community
 * @description: 敏感词过滤，Tire树
 * @author: zjx
 * @create: 2022-06-01 18:59
 **/
@Component
public class SensitiveFilter {
    //替换符
    private final String REPLACEMENT="***";

    //根结点 从根结点开始初始化
    TrieNode root=new TrieNode();



    /**
     * PostConstruct 注释用于在依赖关系注入完成之后需要执行的方法上，以执行任何初始化。
     * 此方法必须在将类放入服务之前调用。支持依赖关系注入的所有类都必须支持此注释
     *
     * 这个注解表示这是一个初始化方法，当容器实例化这个bean以后，调用构造器后就调用这个方法，服务初始化
     */
    @PostConstruct
    public void init(){
        // Classes 目录下读 类加载器是在类路径下加载资源;
//        InputStream is=null;
//        BufferedReader br=null;
//        try {
//            is=this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt"); //没有父目录
//            //字节流 -> 缓冲流
//            br=new BufferedReader(new InputStreamReader(is));
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                is.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                br.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
/// Java7提供的自动关闭流   try(流){流操作}catch{}
        try(InputStream is=this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
            BufferedReader br=new BufferedReader(new InputStreamReader(is))){
            String keyword;
            while((keyword=br.readLine())!=null){
                //添加到前缀树
                this.addKeyword(keyword);
            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    /**
     * 将敏感词添加到前缀树中
     * 一些note:这是在Tried树的类外做的，所以访问private属性 要用到get set方法
     * 当然，我把它改造为在类内做，那我只要调用方法就行了。。。
     * @param keyword 敏感词
     */
    public void addKeyword(String keyword){
        TrieNode node=root;
        node.insertWord(keyword);
    }


    /**
     * 过滤敏感词
     * @param text 待过滤的文本
     * @return 过滤后的文本
     */
    public String filter(String text){
        if(StringUtils.isBlank(text)){
            return null;
        }
        StringBuilder temp=new StringBuilder(text);
        temp.append(" ");
        text=temp.toString();
        //指针1 指向树
        TrieNode node=root;
        //指针2慢 3快 指向字符串
        int begin=0;
        int position=0;
        //结果字符串（使用变长的）
        StringBuilder sb=new StringBuilder();

        while(position<text.length()){
            char c=text.charAt(position);

            //跳过符号  ---严谨的算法~！  比如%开%票%   两个指针就是为了过滤这种问题 （当然，ctf爷爷的那种双写似乎还是只能过滤掉一个）
            if(isSymbol(c)){
                if(node==root){
                    sb.append(c);
                    begin++;
                }
                position++;
                continue;
            }

            node=node.getNext(c);
            if(node == null){
                //以begin开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                //进入下一个位置
                position=++begin;
                //重新根结点
                node=root;
            }else if(node.isKeywordEnd()){
                //已经发现敏感词
                sb.append(REPLACEMENT);
                begin=++position;
                node=root;
            }else{
                //继续检查下一个字符
                position++;
            }
        }
        //将最后一批字符计入结果
        sb.append(text.substring(begin));
        return sb.toString();


    }

    //判断是否为符号
    private boolean isSymbol(Character c){
        return !CharUtils.isAsciiAlphanumeric(c)&&(c<0x2E80||c>0x9FFF);
    }


     private class TrieNode{
        //结束标识符
        private boolean isKeywordEnd;

        // 指向下一结点的指针
        private Map<Character,TrieNode> next=new HashMap<>();

        void insertWord(String word){
            TrieNode node=this;
            for(int i=0;i<word.length();++i){
                char c=word.charAt(i);
                if(node.next.get(c)==null){
                    TrieNode nextNode=new TrieNode();
                    next.put(c,nextNode);
                }
                if(i==word.length()-1) node.isKeywordEnd=true;
                node=node.next.get(c);

            }
            //node.isKeywordEnd=true;
        }

        boolean findWord(String word){
            TrieNode node=this;
            for(int i=0;i<word.length();i++){
                char c=word.charAt(i);
                if(node.next.get(c)!=null){
                    node=node.next.get(c);
                }
                else{
                    break;
                }
            }
            return node.isKeywordEnd;
        }




        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        //添加子结点


        public void setNext(Character c,TrieNode node) {
            next.put(c,node);
        }

        public TrieNode getNext(Character c){
            return next.get(c);
        }


    }
}
