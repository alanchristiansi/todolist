package br.com.achristian.todolist.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class Utils {

    public static void copyNonNullProperties(Object source, Object target){
        //Todos os valores nulo encontrados, vai atribuir aqui
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source, target));
    }

    //Recupera tudo que for Null num update das tasks
    public static String[] getNullPropertyNames(Object source, Object target){
        //BeanWrapper     - Interface que acessa propriedades de um OPbjeto dentro do JAva
        //BeanWrapperImpl - Implementa essa interface
        final BeanWrapper src = new BeanWrapperImpl(source);
        
        //Recupera todas as propriedades 
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        //Cria um array com todos os nomes null/vazios
        Set<String> emptyNames = new HashSet<>();

        //Passa por todos os campos verificando se estão vazios, se estiver, incrementa a variavel do tipo array
        for(PropertyDescriptor pd:pds){
            Object srcValue = src.getPropertyValue(pd.getName());
            if(srcValue == null){
                emptyNames.add(pd.getName());
            }
        }

        String[] result = new String[emptyNames.size()];
        //Retorna String de todos os campos vazios/null
        return emptyNames.toArray(result);
    }

    
}
