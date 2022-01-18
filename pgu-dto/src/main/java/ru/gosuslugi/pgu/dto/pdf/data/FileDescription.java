package ru.gosuslugi.pgu.dto.pdf.data;

import lombok.Data;
import ru.gosuslugi.pgu.dto.ApplicantRole;

import java.util.Map;

@Data
public class FileDescription {

    /** формат генерируемого файла */
    private FileType type;

    /** имя создаваемого файла - виден при скачиваниии и в ЛК */
    private String fileName;

    /** нужно ли добавлять расширение к имени файла**/
    private boolean extensionDisplay = true;

    /** Добавить к имени файла уникальное значение. По умолчанию NONE. */
    private UniqueType addedFileName = UniqueType.NONE;

    /** значение мнемоники для создаваемого файла */
    private String mnemonic;

    /** Добавить к мнемонике уникальное значение. По умолчанию NONE. */
    private UniqueType addedMnemonic = UniqueType.NONE;

    /** Способ сохранения файла. По умолчанию LK */
    private AttachmentType attachmentType = AttachmentType.LK;

    /** Использование шаблонов по ролям */
    private Map<ApplicantRole, String> templates;

    /** Выражение для поиска в applicantAnswers условия добавления файла */
    private String addRule;
}

