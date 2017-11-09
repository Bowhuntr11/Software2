/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software2;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.util.converter.LocalTimeStringConverter;

/**
 *
 * @author SFFPC
 */
public class SpinnerTime extends Spinner{
    
        public SpinnerTime() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            SpinnerValueFactory value = new SpinnerValueFactory<LocalTime>() {
            {
            setConverter(new LocalTimeStringConverter(formatter,null));
            }
            @Override
            public void decrement(int steps) {
                if (this.getValue() == null)
                    setValue(LocalTime.now());
                else {
                    LocalTime time = (LocalTime) getValue();
                    setValue(time.minusMinutes(steps));
                }
            }
            @Override
            public void increment(int steps) {
                if (this.getValue() == null)
                    setValue(LocalTime.now());
                else {
                    LocalTime time = (LocalTime) getValue();
                    setValue(time.plusMinutes(steps));
                }
            }
        };
    }
    
}
