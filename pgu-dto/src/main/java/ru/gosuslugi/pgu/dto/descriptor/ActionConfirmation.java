package ru.gosuslugi.pgu.dto.descriptor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.gosuslugi.pgu.common.core.exception.dto.ModalComponentButton;
import ru.gosuslugi.pgu.common.core.exception.dto.ModalWindowButton;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionConfirmation {
    private String title;
    private String text;
    private List<ModalWindowButton> actionButtons;
    private List<ModalComponentButton> buttons;
}
