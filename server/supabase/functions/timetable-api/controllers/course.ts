import { useConnection } from "../index.ts";

// @deno-types="npm:@types/express@^4.17"
import express from "npm:express@4.18.2";

const course = express.Router();

course.get("/", async (_req, res) => {
    type Row = {
        group_id: string;
        course: number;
        group_name: string;
    };

    type Group = {
        id: string;
        name: string;
    };

    type MergedCourseAndGroup = {
        course: number;
        groups: Group[];
    };

    await useConnection(async (connection) => {
        const result = await connection.queryObject(`--sql
            SELECT c.course, groups.id as group_id, groups.name as group_name
                FROM groups
                JOIN public.courses c on c.course = groups.course
                ORDER BY c.course;
                `);

        const mappedByCourseId: Record<number, MergedCourseAndGroup> = {};

        for (const row of result.rows as Row[]) {
            mappedByCourseId[row.course] ||= {
                course: row.course,
                groups: [],
            };

            mappedByCourseId[row.course].groups.push({
                id: row.group_id,
                name: row.group_name
            });
        }

        const compare = (a: string, b: string): number => {
            if (a.length == b.length) {
                return a.localeCompare(b);
            }

            return a.length - b.length;
        };

        const values = Object.values(mappedByCourseId);

        values.forEach((course) => {
            course.groups.sort((a, b) => compare(a.name, b.name));
        });

        res.json(values);
    });
});

export default course;
