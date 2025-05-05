import { useConnection } from "../index.ts";

// @deno-types="npm:@types/express@^4.17"
import express from "npm:express@4.18.2";

const subgroupSubject = express.Router();

subgroupSubject.get("/group/:group", async (req, res) => {
    await useConnection(async (connection) => {
        const result = await connection.queryObject(
            `--sql
            SELECT DISTINCT
                subgroupsubjects.id,
                subgroupsubjects.name
            FROM subgroupsubjects
                JOIN public.subgroups s on subgroupsubjects.id = s.subject
                JOIN public.lessons l on s.id = l.subgroup
            WHERE l."group" = $1;
            `,
            ["" + req.params.group]
        );
        res.json(result.rows);
    });
});

subgroupSubject.get("/group/:group/all", async (req, res) => {
    type Row = {
        id: number;
        name: string;
        subgroup_id: number;
        subgroup_name: string;
    };

    type SubGroup = {
        id: number;
        name: string;
    };

    type MergedSubGroupSubject = {
        id: number;
        name: string;
        subgroups: SubGroup[];
    };

    await useConnection(async (connection) => {
        const sql = `--sql
        SELECT DISTINCT ON(s.id)
            subgroupsubjects.id,
            subgroupsubjects.name,
            s.id as subgroup_id,
            s.name as subgroup_name
        FROM subgroupsubjects
            JOIN public.subgroups s on subgroupsubjects.id = s.subject
            JOIN public.lessons l on s.id = l.subgroup
        WHERE l."group"  = $1;
        `;
        
        const result = await connection.queryObject(
            sql, ["" + req.params.group]
        );

        const mappedBySubjectId: Record<number, MergedSubGroupSubject> = {};

        for (const row of result.rows as Row[]) {
            mappedBySubjectId[row.id] ||= {
                id: row.id,
                name: row.name,
                subgroups: [],
            };

            mappedBySubjectId[row.id].subgroups.push({
                id: row.subgroup_id,
                name: row.subgroup_name
            });
        }

        res.json(Object.values(mappedBySubjectId));
    });
});

subgroupSubject.get("/:id/subgroups", async (req, res) => {
    await useConnection(async (connection) => {
        const result = await connection.queryObject(
            `--sql
            SELECT * FROM subgroups WHERE subject = $1;
            `,
            [req.params.id]
        );
        res.json(result.rows);
    });
});

export default subgroupSubject;
