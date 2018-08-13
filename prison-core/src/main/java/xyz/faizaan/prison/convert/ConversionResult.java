/*
 * Prison is a Minecraft plugin for the prison game mode.
 * Copyright (C) 2018 The Prison Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package xyz.faizaan.prison.convert;

/**
 * Contains information on the result of the conversion.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class ConversionResult {

    private String agentName;
    private Status status;
    private String reason;

    public ConversionResult(String agentName, Status status, String reason) {
        this.agentName = agentName;
        this.status = status;
        this.reason = reason;
    }

    public static ConversionResult success(String agentName) {
        return new ConversionResult(agentName, Status.Success, "OK");
    }

    public static ConversionResult failure(String agentName, String reason) {
        return new ConversionResult(agentName, Status.Failure, reason);
    }

    public String getAgentName() {
        return agentName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public enum Status {

        Success, Failure;

    }

}
