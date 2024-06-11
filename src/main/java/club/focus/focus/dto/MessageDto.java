package club.focus.focus.dto;

import java.util.ArrayList;

public record MessageDto(ArrayList<String> sections,
                      String timeStamp,
                      String textBody,
                      int hash) {}
