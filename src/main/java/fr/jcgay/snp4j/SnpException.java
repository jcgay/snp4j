package fr.jcgay.snp4j;

import fr.jcgay.snp4j.impl.response.Error;
import fr.jcgay.snp4j.impl.response.Status;
import lombok.Getter;

/**
 * Indicate that an unexpected error occurs.
 */
public class SnpException extends RuntimeException {

    /**
     * Give error context (if any) based on Snarl error codes.
     *
     * @see <a href="https://sites.google.com/site/snarlapp/developers/api-reference#TOC-Errors">Snarl errors</a>
     */
    @Getter
    private Status status;

    public SnpException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SnpException(Error error) {
        super(error.getHint());
        this.status = error.getStatus();
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(super.toString())
                .append("\n")
                .append(" Status: ")
                .append(status != null ? status : "NONE")
                .toString();
    }
}
