package com.tn.dto;

import com.tn.domain.Setmeal;
import com.tn.domain.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
